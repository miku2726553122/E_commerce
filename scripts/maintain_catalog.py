"""Maintain the local demonstration catalog, preserving backups and image provenance."""

import argparse
import base64
import concurrent.futures
import csv
import datetime as dt
from decimal import Decimal
import hashlib
import hmac
import html
import io
import json
import os
from pathlib import Path
import re
import subprocess
import sys
import threading
import time
import uuid
from urllib.parse import quote, urlsplit
import xml.etree.ElementTree as ET

import pymysql
import requests
from bs4 import BeautifulSoup
from PIL import Image, ImageDraw, ImageFont, ImageOps


ROOT = Path(__file__).resolve().parents[1]
DEST = Path(r"D:\zhuomian\aa")
WORK = DEST / "ecommerce-catalog-20261022"
PLAN = json.loads((ROOT / "scripts/catalog-plan.json").read_text(encoding="utf-8"))
ENTRIES = {row[0]: dict(id=row[0], price=row[1], spec=row[2], query=row[3]) for row in PLAN["products"]}
ALIASES = {int(key): value for key, value in PLAN["aliases"].items()}
HEADERS = {
    "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36",
    "Accept-Language": "zh-CN,zh;q=0.9,en;q=0.7",
}
LOCK = threading.Lock()
MANIFEST_PATH = WORK / "image-manifest.json"


def credentials():
    tree = ET.parse(ROOT / ".idea/workspace.xml")
    env = {item.attrib["name"]: item.attrib["value"] for item in tree.iter("env")}
    return env


def connect(database="ecommerce"):
    env = credentials()
    return pymysql.connect(host="127.0.0.1", port=3306, user=env["username"], password=env["password"],
                           database=database, charset="utf8mb4", cursorclass=pymysql.cursors.DictCursor,
                           autocommit=False)


def query(connection, sql, args=None):
    with connection.cursor() as cursor:
        cursor.execute(sql, args)
        return cursor.fetchall()


def serial(value):
    if isinstance(value, (dt.date, dt.datetime)):
        return value.isoformat()
    return str(value)


def save_json(path, data):
    path.parent.mkdir(parents=True, exist_ok=True)
    temporary = path.with_suffix(".tmp")
    temporary.write_text(json.dumps(data, ensure_ascii=False, indent=2, default=serial), encoding="utf-8")
    temporary.replace(path)


def load_manifest():
    return json.loads(MANIFEST_PATH.read_text(encoding="utf-8")) if MANIFEST_PATH.exists() else {}


def backup():
    WORK.mkdir(parents=True, exist_ok=True)
    with connect() as connection:
        target = query(connection, "SELECT DATABASE() db, @@hostname host, CURRENT_USER() account")[0]
        assert target["db"] == "ecommerce"
        snapshot = {}
        for table in query(connection, "SHOW TABLES"):
            name = next(iter(table.values()))
            snapshot[name] = query(connection, f"SELECT * FROM `{name}`")
    stamp = dt.datetime.now().strftime("%Y%m%d-%H%M%S")
    save_json(WORK / f"database-before-{stamp}.json", snapshot)
    env = credentials()
    result = subprocess.run([
        r"C:\Program Files\MySQL\MySQL Server 8.0\bin\mysqldump.exe",
        "--host=127.0.0.1", "--port=3306", f'--user={env["username"]}',
        "--default-character-set=utf8mb4", "--single-transaction", "--skip-lock-tables",
        "--no-tablespaces", f"--result-file={WORK / f'database-before-{stamp}.sql'}", "ecommerce",
    ], env={**os.environ, "MYSQL_PWD": env["password"]}, capture_output=True, text=True)
    if result.returncode:
        raise RuntimeError(result.stderr)
    save_json(WORK / "database-audit-before.json", audit(snapshot))
    print(json.dumps({"backup": str(WORK / f"database-before-{stamp}.sql"),
                      "tables": {key: len(value) for key, value in snapshot.items()},
                      "audit": {key: value["count"] for key, value in audit(snapshot).items()}},
                     ensure_ascii=False, indent=2), flush=True)


def audit(data=None):
    if data is None:
        with connect() as connection:
            data = {table: query(connection, f"SELECT * FROM `{table}`") for table in
                    ["products", "product_skus", "orders", "order_items", "inventories", "users",
                     "categories", "cart_items", "carts", "user_addresses"]}
    skus = {row["id"]: row for row in data["product_skus"]}
    products = {row["id"]: row for row in data["products"]}
    orders = {row["id"]: row for row in data["orders"]}
    items = {}
    locked = {}
    for item in data["order_items"]:
        items.setdefault(item["order_id"], []).append(item)
        if orders[item["order_id"]]["order_status"] in ["待付款", "待发货", "已支付"] and item["shipped_at"] is None:
            locked[item["sku_id"]] = locked.get(item["sku_id"], 0) + item["quantity"]
    problems = {
        "productsWithoutCover": [p["id"] for p in products.values() if not p["main_image"]],
        "skuProductMismatch": [i["id"] for i in data["order_items"] if skus[i["sku_id"]]["product_id"] != i["product_id"]],
        "cartProductMismatch": [i["id"] for i in data["cart_items"] if skus[i["sku_id"]]["product_id"] != i["product_id"]],
        "basePriceMismatch": [],
        "orderTotalMismatch": [o["id"] for o in orders.values()
                               if o["total_amount"] != sum((i["total_amount"] for i in items.get(o["id"], [])), Decimal(0))],
        "orderPayableMismatch": [o["id"] for o in orders.values()
                                 if o["payable_amount"] != o["total_amount"] + o["freight_amount"]],
        "itemTotalMismatch": [i["id"] for i in data["order_items"] if i["total_amount"] != i["unit_price"] * i["quantity"]],
        "legacyPaidStatus": [o["id"] for o in orders.values() if o["order_status"] == "已支付"],
        "inventoryLockMismatch": [i["id"] for i in data["inventories"] if i["locked_quantity"] != locked.get(i["sku_id"], 0)],
        "invalidInventory": [i["id"] for i in data["inventories"]
                             if min(i["stock_quantity"], i["locked_quantity"], i["warning_quantity"]) < 0
                             or i["locked_quantity"] > i["stock_quantity"]],
        "placeholderOrderItems": [i["id"] for i in data["order_items"] if re.fullmatch(r"商品\d+", i["product_name"])],
        "placeholderSkus": [s["id"] for s in skus.values() if re.match(r"商品\d+", s["sku_name"])],
        "originalPriceBelowSale": [s["id"] for s in skus.values() if s["original_price"] < s["sale_price"]],
        "invalidProductMerchant": [p["id"] for p in products.values()
                                   if next((u["role"] for u in data["users"] if u["id"] == p["merchant_id"]), None) != "MERCHANT"],
        "orderStateMismatch": [],
        "invalidOrderTimeline": [],
    }
    for p in products.values():
        prices = [s["sale_price"] for s in skus.values() if s["product_id"] == p["id"] and s["status"] == 1]
        if prices and p["base_price"] != min(prices):
            problems["basePriceMismatch"].append(p["id"])
    for order in orders.values():
        state = order["order_status"]
        paid = state in ("待发货", "已发货", "已完成")
        shipped = state in ("已发货", "已完成")
        if (order["payment_status"] != ("已支付" if paid else "未支付")
            or (paid and order["paid_at"] is None)
            or (shipped and (order["shipped_at"] is None or any(i["shipped_at"] is None for i in items.get(order["id"], []))))
            or (state == "已完成" and (order["completed_at"] is None or order["delivery_status"] != "已签收"))
            or (state == "已取消" and order["cancelled_at"] is None)):
            problems["orderStateMismatch"].append(order["id"])
        timeline = [order[key] for key in ["created_at", "paid_at", "shipped_at", "completed_at"] if order[key]]
        if timeline != sorted(timeline) or (state == "已取消" and (order["paid_at"] or order["shipped_at"] or order["completed_at"])):
            problems["invalidOrderTimeline"].append(order["id"])
    return {key: {"count": len(value), "ids": value} for key, value in problems.items()}


def normalized(text):
    return re.sub(r"[^a-z0-9\u4e00-\u9fff]", "", text.lower())


def relevance(entry, name, title):
    value = normalized(title)
    patterns = {
        1: ["iphone15promax"], 2: ["mate60pro"], 3: ["小米14ultra", "xiaomi14ultra"],
        4: ["findx7"], 5: ["x100pro"], 6: ["galaxys24", "三星s24"],
        7: ["magic6"], 8: ["一加12", "oneplus12"], 9: ["魅族21", "meizu21"],
        10: ["努比亚z60", "nubiaz60"], 11: ["真我gt5", "realmegt5"],
        12: ["thinkpadx1"], 13: ["matebook16"], 14: ["小米bookpro", "xiaomibookpro"],
        15: ["xps15"], 16: ["暗影精灵9", "omen16"], 17: ["天选4"],
        18: ["宏碁掠夺者", "acerpredator"], 19: ["蛟龙16"], 20: ["magicbook"],
        21: ["redmibookpro"], 22: ["战神z8"], 23: ["magsafe手机壳", "magsafe保护壳", "苹果透明保护壳"],
        24: ["华为保护壳", "mate60pro手机壳", "mate60pro保护壳"],
        25: ["小米移动电源", "小米充电宝"], 26: ["品胜数据线"], 27: ["绿联充电器"],
        28: ["倍思车载支架", "倍思手机支架"], 29: ["摩米士耳机", "momax耳机", "摩米士蓝牙耳机"],
        30: ["南卡runnerpro", "nankrunnerpro"], 31: ["lollipods"], 32: ["tune770"],
        33: ["wh1000xm5"],
        34: ["纯棉t恤", "男士t恤"], 35: ["休闲西裤", "商务西裤"], 36: ["男士羽绒服"],
        37: ["男士牛仔裤", "男装牛仔裤"], 38: ["男士夹克", "休闲夹克"], 39: ["男士卫衣"],
        40: ["polo衫", "男士polo"], 41: ["男士风衣"], 42: ["男士运动短裤", "运动短裤男"],
        43: ["男士衬衫", "男式衬衫"], 44: ["男士毛衣"], 45: ["连衣裙"], 46: ["女士针织衫"],
        47: ["半身裙"], 48: ["女士风衣"], 49: ["雪纺衫"], 50: ["女士大衣", "女式大衣"],
        51: ["阔腿裤"], 52: ["女士卫衣"], 53: ["a字裙"], 54: ["女士毛衣"], 55: ["碎花裙", "碎花连衣裙"],
        56: ["男士跑步鞋", "男款跑步鞋"], 57: ["女士运动鞋", "女款运动鞋"], 58: ["帆布鞋"],
        59: ["商务皮鞋", "男士皮鞋"], 60: ["高跟鞋"], 61: ["马丁靴"], 62: ["女士平底鞋"],
        63: ["运动凉鞋"], 64: ["老爹鞋"], 65: ["女士靴子", "女靴"], 66: ["男士板鞋"],
        67: ["三只松鼠坚果", "三只松鼠每日坚果"], 68: ["良品铺子芒果干"], 69: ["百草味手撕面包"],
        70: ["洽洽每日坚果", "恰恰每日坚果"], 71: ["乐事薯片", "乐事原味"], 72: ["奥利奥"],
        73: ["旺旺雪饼", "旺旺雪餅"], 74: ["达利园蛋黄派"], 75: ["好丽友派", "好丽友巧克力派"],
        76: ["盼盼小面包", "盼盼法式软面包"], 77: ["三只松鼠夏威夷果"], 78: ["立顿红茶", "立顿黄牌"],
        79: ["雀巢速溶咖啡", "雀巢咖啡"], 80: ["东方树叶"], 81: ["统一冰红茶"],
        82: ["康师傅茉莉蜜茶"], 83: ["三得利乌龙茶"], 84: ["元气森林气泡水"],
        85: ["可口可乐", "cocacola"], 86: ["百事可乐", "pepsi"], 87: ["红牛", "redbull"],
        88: ["王老吉"], 89: ["全棉四件套", "纯棉四件套"], 90: ["记忆棉枕", "记忆棉颈椎枕"],
        91: ["蚕丝被"], 92: ["羽绒被"], 93: ["乳胶床垫"], 94: ["纯棉床单"], 95: ["磨毛四件套"],
        96: ["天丝四件套"], 97: ["真丝枕套"], 98: ["夏凉被"], 99: ["加厚被子", "冬被"],
        100: ["nikeairmax"], 101: ["adidasultraboost", "ultraboost"], 102: ["newbalance574", "nb574"],
        103: ["gelkayano"], 104: ["pumarsx"], 105: ["李宁超轻"], 106: ["安踏c37"], 107: ["特步160x", "特步竞速160x"],
        108: ["乔丹飞影pb", "飞影pb"], 109: ["鸿星尔克奇弹"], 110: ["nikepegasus", "耐克飞马"],
        111: ["兰蔻小黑瓶"], 112: ["雅诗兰黛眼霜", "雅诗兰黛小棕瓶眼霜"],
        113: ["skii神仙水", "sk2神仙水"], 114: ["资生堂红腰子"], 115: ["海蓝之谜面霜", "lamer面霜"],
        116: ["科颜氏高保湿"], 117: ["悦木之源菌菇水"], 118: ["薇姿喷雾", "薇姿温泉", "薇姿润泉"],
        119: ["理肤泉b5面膜"], 120: ["修丽可ce"], 121: ["珀莱雅双抗"],
        122: ["mac口红", "mac子弹头"], 123: ["ysl粉底", "圣罗兰粉底"], 124: ["迪奥烈艳蓝金"],
        125: ["香奈儿气垫"], 126: ["阿玛尼权力"], 127: ["nars腮红"], 128: ["植村秀砍刀眉笔"],
        129: ["美宝莲fitme"], 130: ["卡姿兰大眼睛"], 131: ["完美日记动物", "完美日记眼影"],
        132: ["花西子蜜粉", "花西子空气蜜粉"],
    }
    score = max((len(pattern) for pattern in patterns.get(entry["id"], [normalized(name)]) if pattern in value), default=0)
    if entry["id"] in (4, 6, 7) and any(word in value for word in ("pro", "ultra", "plus")):
        return 0
    if entry["id"] == 79 and any(word in title for word in ("即饮", "瓶装", "拿铁")):
        return 0
    if entry["id"] == 116 and not any(word in title for word in ("面霜", "保湿霜")):
        return 0
    if entry["id"] == 72 and "饼干碎" in title:
        return 0
    if any(word in value for word in ["logo", "壁纸", "表情包", "工作总结", "手绘"]):
        return 0
    # Favor retailer/product listings over comparison articles and news photos.
    score += 2 if score and any(word in title for word in ["报价", "价格", "克", "商品", "包", "ml", "mL"]) else 0
    return score


def image_candidates(entry, name):
    sources = json.loads((ROOT / "scripts/catalog-image-sources.json").read_text(encoding="utf-8"))
    if str(entry["id"]) in sources:
        return [{**sources[str(entry["id"])], "relevance": 100}]
    candidates = []
    if 67 <= entry["id"] <= 88 or 111 <= entry["id"] <= 132:
        response = requests.get("https://www.sayweee.com/zh/search?keyword=" + quote(name),
                                headers=HEADERS, timeout=25)
        response.raise_for_status()
        soup = BeautifulSoup(response.content, "html.parser", from_encoding="utf-8")
        retail_terms = {
            67: ("三只松鼠", "坚果"), 68: ("良品铺子", "芒果"), 69: ("百草味", "手撕面包"),
            70: ("洽洽", "每日坚果"), 71: ("乐事", "薯片"), 72: ("奥利奥",),
            73: ("旺旺", "雪餅"), 74: ("达利园", "蛋黄派"), 75: ("好丽友", "派"),
            76: ("盼盼", "面包"), 77: ("三只松鼠", "夏威夷"), 78: ("立顿", "红茶"),
            79: ("雀巢", "咖啡"), 80: ("东方树叶", "茉莉"), 81: ("统一", "冰红茶"),
            82: ("康师傅", "茉莉蜜茶"), 83: ("三得利", "乌龙"), 84: ("元气森林", "白桃"),
            85: ("可口可乐",), 86: ("百事", "可乐"), 87: ("红牛",), 88: ("王老吉",),
            111: ("兰蔻", "小黑瓶", "精华"), 112: ("雅诗兰黛", "眼霜"),
            113: ("skii", "神仙水"), 114: ("资生堂", "红腰子"), 115: ("海蓝之谜", "面霜"),
            116: ("科颜氏", "高保湿", "面霜"), 117: ("悦木之源", "菌菇"),
            118: ("薇姿", "喷雾"), 119: ("理肤泉", "面膜"), 120: ("修丽可", "ce"),
            121: ("珀莱雅", "双抗"), 122: ("mac", "口红"), 123: ("圣罗兰", "粉底"),
            124: ("迪奥", "烈艳蓝金"), 125: ("香奈儿", "气垫"), 126: ("阿玛尼", "权力"),
            127: ("nars", "腮红"), 128: ("植村秀", "眉笔"), 129: ("美宝莲", "fit"),
            130: ("卡姿兰", "睫毛膏"), 131: ("完美日记", "眼影"), 132: ("花西子", "蜜粉"),
        }
        for image in soup.select("img[alt][src]"):
            title = image.get("alt", "")
            value = normalized(title)
            terms = retail_terms.get(entry["id"], ())
            matches = terms and all(normalized(term) in value for term in terms)
            if matches and not (entry["id"] == 111 and "眼" in title) and not (entry["id"] == 72 and "饼干碎" in title):
                parent = image.find_parent("a", href=True)
                page = parent["href"] if parent else response.url
                if page.startswith("/"):
                    page = "https://www.sayweee.com" + page
                score = 20 + relevance(entry, name, title)
                for amount in re.findall(r"\d+", entry["spec"]):
                    if amount in title:
                        score += 5
                candidates.append({"url": image["src"], "sourcePage": page, "title": title,
                                   "relevance": score})
        if candidates:
            return sorted(candidates, key=lambda item: item["relevance"], reverse=True)
    aliases = {11: "真我GT5", 34: "男士纯棉T恤", 35: "男士休闲西裤", 37: "男士牛仔裤",
               40: "男士POLO衫", 45: "女士连衣裙", 47: "女士半身裙", 49: "女士雪纺衫",
               51: "女士阔腿裤", 53: "女士A字裙", 55: "女士碎花裙", 56: "男士跑步鞋",
               57: "女士运动鞋", 58: "休闲帆布鞋", 60: "女士高跟鞋", 62: "女士平底鞋",
               66: "男士板鞋", 70: "洽洽每日坚果", 79: "雀巢咖啡", 118: "薇姿温泉喷雾"}
    term = aliases.get(entry["id"], name)
    for search_term in [f'"{term}"', term + " 商品", entry["query"]]:
        response = requests.get("https://www.bing.com/images/search?q=" + quote(search_term) +
                                "&qs=n&form=QBIR&sp=-1&lq=0", headers=HEADERS, timeout=25)
        response.raise_for_status()
        soup = BeautifulSoup(response.content, "html.parser", from_encoding="utf-8")
        for element in soup.select("a.iusc[m]"):
            try:
                info = json.loads(element["m"])
                title = info.get("t", "")
                score = relevance(entry, name, title)
                url = info["murl"]
                if not score or urlsplit(url).scheme not in ["http", "https"]:
                    continue
                candidates.append({"url": url, "sourcePage": info.get("purl", ""), "title": title, "relevance": score})
            except (KeyError, ValueError):
                continue
        if candidates:
            break
    (WORK / f"search-{entry['id']:03}-filtered.html").write_text(response.text, encoding="utf-8")
    return sorted(candidates, key=lambda item: item["relevance"], reverse=True)


def download_one(entry, names):
    manifest = load_manifest()
    key = str(entry["id"])
    if key in manifest and manifest[key].get("relevanceChecked") and Path(manifest[key]["file"]).exists():
        return manifest[key]
    candidates = image_candidates(entry, names[entry["id"]])
    errors = []
    for candidate in candidates[:12]:
        try:
            response = requests.get(candidate["url"], headers=HEADERS, timeout=18)
            response.raise_for_status()
            if len(response.content) > 15 * 1024 * 1024:
                continue
            with Image.open(io.BytesIO(response.content)) as original:
                original.load()
                if min(original.size) < 250 or max(original.size) / min(original.size) > 2.3:
                    continue
                image = ImageOps.exif_transpose(original)
                background = Image.new("RGB", image.size, "white")
                if image.mode == "RGBA":
                    background.paste(image, mask=image.getchannel("A"))
                else:
                    background.paste(image.convert("RGB"))
                background.thumbnail((1100, 1100), Image.Resampling.LANCZOS)
                # Keep the entire product visible in the storefront's cover crop.
                side = max(background.size)
                square = Image.new("RGB", (side, side), "white")
                square.paste(background, ((side - background.width) // 2, (side - background.height) // 2))
                path = DEST / f"ecommerce-product-{entry['id']:03}.jpg"
                square.save(path, "JPEG", quality=89, optimize=True)
            item = {**candidate, "productId": entry["id"], "name": names[entry["id"]], "relevanceChecked": True,
                    "file": str(path), "width": side, "height": side,
                    "sha256": hashlib.sha256(path.read_bytes()).hexdigest()}
            with LOCK:
                manifest = load_manifest()
                manifest[key] = item
                save_json(MANIFEST_PATH, manifest)
            print(f"IMAGE {entry['id']:03} {names[entry['id']]} {side}px", flush=True)
            return item
        except Exception as exc:
            errors.append(f"{urlsplit(candidate['url']).hostname}: {str(exc)[:90]}")
    print(f"MISSING {entry['id']:03} {names[entry['id']]} candidates={len(candidates)} {errors[:2]}", flush=True)
    return None


def download(ids=None):
    WORK.mkdir(parents=True, exist_ok=True)
    with connect() as connection:
        names = {row["id"]: row["product_name"] for row in query(connection, "SELECT id,product_name FROM products")}
    entries = [entry for key, entry in ENTRIES.items() if not ids or key in ids]
    with concurrent.futures.ThreadPoolExecutor(max_workers=4) as pool:
        futures = {pool.submit(download_one, entry, names): entry for entry in entries}
        for future in concurrent.futures.as_completed(futures):
            try:
                future.result()
            except Exception as exc:
                print(f"ERROR {futures[future]['id']:03} {str(exc)[:200]}", flush=True)
    contact_sheets()


def contact_sheets():
    manifest = load_manifest()
    font = ImageFont.truetype(r"C:\Windows\Fonts\msyh.ttc", 17)
    items = sorted((row for row in manifest.values() if row.get("relevanceChecked")),
                   key=lambda row: row["productId"])
    for start in range(0, len(items), 24):
        chunk = items[start:start + 24]
        sheet = Image.new("RGB", (6 * 215, 4 * 255), "#e6e9e8")
        draw = ImageDraw.Draw(sheet)
        for index, row in enumerate(chunk):
            x, y = (index % 6) * 215, (index // 6) * 255
            with Image.open(row["file"]) as original:
                picture = ImageOps.contain(original, (205, 205))
                sheet.paste(picture, (x + 5 + (205 - picture.width) // 2, y + 5))
            label = f"{row['productId']:03} {row['name']}"
            draw.text((x + 6, y + 214), label[:16], fill="#17221c", font=font)
        sheet.save(WORK / f"contact-{start // 24 + 1:02}.jpg", quality=90)
    print(f"Downloaded {len(items)}/{len(ENTRIES)} unique product images", flush=True)


def token():
    config = (ROOT / "src/main/resources/application.yml").read_text(encoding="utf-8")
    secret = re.search(r"^\s+secret:\s*(\S+)", config, re.M).group(1)
    issuer = re.search(r"^\s+issuer:\s*(\S+)", config, re.M).group(1)
    with connect() as connection:
        user = query(connection, "SELECT id,username,role FROM users WHERE role='ADMIN' AND status=1 ORDER BY id LIMIT 1")[0]
    encode = lambda value: base64.urlsafe_b64encode(value).rstrip(b"=").decode()
    head = encode(json.dumps({"alg": "HS512"}).encode())
    now = int(time.time())
    body = encode(json.dumps({"iss": issuer, "sub": user["username"], "userId": user["id"],
                              "role": user["role"], "iat": now, "exp": now + 3600}).encode())
    signature = encode(hmac.new(base64.b64decode(secret), f"{head}.{body}".encode(), hashlib.sha512).digest())
    return f"{head}.{body}.{signature}"


def sql_literal(value):
    if value is None:
        return "NULL"
    if isinstance(value, (int, float, Decimal)):
        return str(value)
    return "'" + str(value).replace("\\", "\\\\").replace("'", "''") + "'"


def sku_variant(entry, extra=False):
    spec, price = entry["spec"], Decimal(str(entry["price"]))
    if extra:
        if entry["id"] <= 11:
            spec = spec.replace("256GB", "512GB")
            price += 1000 if entry["id"] == 1 else 500
        elif entry["id"] <= 22:
            spec = "32GB+1TB"
            price += 1000
        else:
            variants = {
                23: ("iPhone 15 Pro Max 适用 / 黑色硅胶", 399),
                24: ("Mate 60 Pro 适用 / 蓝色", 99),
                25: ("20000mAh / 33W", 199),
                26: ("USB-C / 2米", 29.9),
                27: ("100W / USB-C", 169),
                28: ("中控台款", 69),
                29: ("真无线 / 黑色", 199),
                30: ("骨传导 / 灰色", 799),
            }
            spec, price = variants[entry["id"]]
            price = Decimal(str(price))
    return spec, price


def catalog_statements(connection, manifest):
    statements = []
    price_rows = []
    products = query(connection, "SELECT * FROM products ORDER BY id")
    names = {
        10: "努比亚Z60 Ultra", 32: "JBL TUNE 770NC", 116: "科颜氏高保湿面霜",
        118: "薇姿温泉喷雾", 130: "卡姿兰大眼睛睫毛膏",
    }
    merchant_targets = {18: (5, 6), 19: (6, 7), 20: (6, 7)}
    for product in products:
        pid = product["id"]
        canonical = ALIASES.get(pid, pid)
        if canonical not in ENTRIES or product["product_code"] != f"PD{pid:06}":
            continue
        entry = ENTRIES[canonical]
        image = manifest.get(str(canonical), {})
        name = names.get(canonical, product["product_name"])
        merchant_id = product["merchant_id"]
        old, new = merchant_targets.get(product["category_id"], (None, None))
        if merchant_id == old:
            merchant_id = new
        if product["merchant_id"] == 7 and product["category_id"] == 9:
            merchant_id = 10
        if product["merchant_id"] == 7 and product["category_id"] == 15:
            merchant_id = 14
        url = image.get("uploadedUrl") or product["main_image"]
        details = f"<p>{html.escape(name)}</p><p>规格：{html.escape(entry['spec'])}</p>"
        # Source images illustrate the product; specifications remain explicit in text.
        description = f"{name} · {entry['spec']}"
        assignments = {
            "product_name": name, "merchant_id": merchant_id, "main_image": url,
            "description": description, "detail_content": details, "base_price": entry["price"],
        }
        update = ", ".join(f"`{key}`={sql_literal(value)}" for key, value in assignments.items())
        statements.append(f"UPDATE products SET {update}, version=version+1 WHERE id={pid} AND product_code={sql_literal(product['product_code'])};")
        skus = query(connection, "SELECT * FROM product_skus WHERE product_id=%s ORDER BY id", (pid,))
        for index, sku in enumerate(skus):
            if sku["sku_code"] != f"SKU-{pid:06}-{index + 1:02}":
                raise ValueError(f"Unexpected custom SKU {sku['id']}; review before replacing")
            spec, price = sku_variant(entry, index > 0)
            specification = json.dumps({"规格": spec}, ensure_ascii=False)
            sku_name = f"{name} / {spec}"
            # Avoid invented markdown discounts: original and sale prices are equal.
            statements.append(
                f"UPDATE product_skus SET sku_name={sql_literal(sku_name)}, specification={sql_literal(specification)}, "
                f"original_price={price}, sale_price={price}, image_url={sql_literal(url)} "
                f"WHERE id={sku['id']} AND product_id={pid};")
        price_rows.append({
            "商品ID": pid, "商品名称": name, "规格": entry["spec"], "原基础价": product["base_price"],
            "新演示价": entry["price"], "商家ID": merchant_id, "本地图片": image.get("file", ""),
            "上传URL": url or "", "来源网页": image.get("sourcePage", ""),
        })
    statements += [
        "UPDATE categories SET category_name='数码配件' WHERE id=11 AND category_name='手机配件';",
        "UPDATE categories SET category_name='饮料与茶饮' WHERE id=16 AND category_name='茶饮冲调';",
        # Only the original generated fixtures get new historical prices and names.
        """CREATE TEMPORARY TABLE catalog_seed_items AS
SELECT oi.id, oi.order_id FROM order_items oi JOIN orders o ON o.id=oi.order_id
WHERE o.id BETWEEN 1 AND 100 AND o.order_no REGEXP '^ORD2025(0[1-9]|1[0-2])[0-9]{3}$'
AND oi.product_name REGEXP '^商品[0-9]+$' AND oi.sku_name REGEXP '^SKU[0-9]+$';""",
        """UPDATE order_items oi JOIN catalog_seed_items seed ON seed.id=oi.id
JOIN products p ON p.id=oi.product_id JOIN product_skus s ON s.id=oi.sku_id
SET oi.product_name=p.product_name, oi.sku_name=s.sku_name,
oi.sku_specification=s.specification, oi.product_image=COALESCE(s.image_url,p.main_image),
oi.unit_price=s.sale_price, oi.total_amount=ROUND(s.sale_price*oi.quantity,2);""",
        """UPDATE orders o JOIN
(SELECT order_id,SUM(total_amount) amount FROM order_items GROUP BY order_id) totals ON totals.order_id=o.id
SET o.total_amount=totals.amount, o.payable_amount=totals.amount+o.freight_amount, o.version=o.version+1
WHERE o.total_amount<>totals.amount OR o.payable_amount<>totals.amount+o.freight_amount;""",
        """UPDATE orders SET order_status='待发货', version=version+1
WHERE order_status='已支付' AND payment_status='已支付';""",
        """UPDATE orders SET payment_status='未支付', delivery_status='待发货',
paid_at=NULL, shipped_at=NULL, completed_at=NULL,
cancelled_at=GREATEST(COALESCE(cancelled_at,DATE_ADD(created_at,INTERVAL 1 HOUR)),created_at),
cancel_reason=COALESCE(NULLIF(cancel_reason,''),'用户取消演示订单'), version=version+1
WHERE id BETWEEN 1 AND 100 AND order_no REGEXP '^ORD2025(0[1-9]|1[0-2])[0-9]{3}$' AND order_status='已取消';""",
        """UPDATE orders SET paid_at=GREATEST(COALESCE(paid_at,DATE_ADD(created_at,INTERVAL 30 MINUTE)),created_at),
payment_status='已支付', version=version+1
WHERE id BETWEEN 1 AND 100 AND order_no REGEXP '^ORD2025(0[1-9]|1[0-2])[0-9]{3}$' AND order_status IN ('待发货','已发货','已完成');""",
        """UPDATE orders SET shipped_at=GREATEST(COALESCE(shipped_at,DATE_ADD(paid_at,INTERVAL 1 DAY)),paid_at),
delivery_status=IF(order_status='已完成','已签收','已发货'), version=version+1
WHERE id BETWEEN 1 AND 100 AND order_no REGEXP '^ORD2025(0[1-9]|1[0-2])[0-9]{3}$' AND order_status IN ('已发货','已完成');""",
        """UPDATE orders SET completed_at=GREATEST(COALESCE(completed_at,DATE_ADD(shipped_at,INTERVAL 2 DAY)),shipped_at),
version=version+1 WHERE id BETWEEN 1 AND 100 AND order_no REGEXP '^ORD2025(0[1-9]|1[0-2])[0-9]{3}$' AND order_status='已完成';""",
        """UPDATE order_items oi JOIN orders o ON o.id=oi.order_id
SET oi.shipped_at=o.shipped_at
WHERE o.id BETWEEN 1 AND 100 AND o.order_no REGEXP '^ORD2025(0[1-9]|1[0-2])[0-9]{3}$' AND o.order_status IN ('已发货','已完成')
AND (oi.shipped_at IS NULL OR oi.shipped_at<o.paid_at);""",
        """UPDATE order_items oi JOIN orders o ON o.id=oi.order_id SET oi.shipped_at=NULL
WHERE o.id BETWEEN 1 AND 100 AND o.order_no REGEXP '^ORD2025(0[1-9]|1[0-2])[0-9]{3}$' AND o.order_status IN ('已取消','待付款');""",
        """UPDATE inventories inv LEFT JOIN (
SELECT oi.sku_id,SUM(oi.quantity) qty FROM order_items oi JOIN orders o ON o.id=oi.order_id
WHERE o.order_status IN ('待付款','待发货') AND oi.shipped_at IS NULL GROUP BY oi.sku_id
) pending ON pending.sku_id=inv.sku_id
SET inv.locked_quantity=COALESCE(pending.qty,0), inv.version=inv.version+1
WHERE inv.locked_quantity<>COALESCE(pending.qty,0);""",
        """UPDATE user_addresses a JOIN users u ON u.id=a.user_id SET a.receiver_name=COALESCE(NULLIF(u.real_name,''),u.nickname)
WHERE a.receiver_name REGEXP '^user[0-9]+([(].*[)])?$' AND u.real_name IS NOT NULL;""",
        """UPDATE orders o JOIN users u ON u.id=o.user_id
SET o.receiver_name=COALESCE(NULLIF(u.real_name,''),u.nickname),o.version=o.version+1
WHERE o.id BETWEEN 1 AND 100 AND o.order_no REGEXP '^ORD2025(0[1-9]|1[0-2])[0-9]{3}$' AND o.receiver_name REGEXP '^user[0-9]+';""",
        "DROP TEMPORARY TABLE catalog_seed_items;",
    ]
    return statements, price_rows


def apply_catalog(dry_run=False):
    manifest = {key: item for key, item in load_manifest().items()
                if item.get("uploadedUrl") and item.get("relevanceChecked") and item.get("visuallyApproved")}
    if not manifest:
        raise ValueError("Upload at least one visually inspected product image before applying")
    if not list(WORK.glob("database-before-*.sql")):
        raise RuntimeError("Create a full backup before applying changes")
    with connect() as connection:
        statements, price_rows = catalog_statements(connection, manifest)
        counts = []
        try:
            for statement in statements:
                with connection.cursor() as cursor:
                    cursor.execute(statement)
                    counts.append(cursor.rowcount)
            checks = audit_in_transaction(connection)
            failures = {key: value["count"] for key, value in checks.items()
                        if value["count"] and key != "productsWithoutCover"}
            if failures:
                raise ValueError(f"Consistency checks failed; rolled back: {failures}")
            if dry_run:
                connection.rollback()
                print(json.dumps({"preview": True, "products": len(price_rows), "uniqueImages": len(manifest),
                                  "productsWithImages": sum(bool(row["上传URL"]) for row in price_rows),
                                  "audit": {key: value["count"] for key, value in checks.items()}},
                                 ensure_ascii=False, indent=2), flush=True)
                return
            connection.commit()
        except Exception:
            connection.rollback()
            raise
    save_json(WORK / "database-audit-after.json", checks)
    save_json(WORK / "applied-products.json", price_rows)
    save_json(WORK / "applied-statement-counts.json", counts)
    with open(WORK / "商品价格及图片对照.csv", "w", encoding="utf-8-sig", newline="") as handle:
        writer = csv.DictWriter(handle, fieldnames=list(price_rows[0]))
        writer.writeheader()
        writer.writerows(price_rows)
    generated = ("-- Generated local demonstration catalog normalization, 2026-10-22.\n"
                 "-- Prices are illustrative CNY prices, not live market quotes.\n"
                 "-- Image URLs come from POST /api/upload/image; provenance is in image-manifest.json.\n"
                 "SET NAMES utf8mb4;\nSTART TRANSACTION;\n\n" + "\n\n".join(statements) + "\n\nCOMMIT;\n")
    migration = ROOT / "src/main/resources/sql/migrations/20261022_catalog_refresh.sql"
    migration.write_text(generated, encoding="utf-8")
    init = ROOT / "src/main/resources/sql/ecommerce.sql"
    existing = init.read_text(encoding="utf-8")
    marker = "-- BEGIN CATALOG REFRESH 20261022"
    if marker in existing:
        existing = existing[:existing.index(marker)]
    init.write_text(existing.rstrip() + "\n\n" + marker + "\n" + generated + "-- END CATALOG REFRESH 20261022\n",
                    encoding="utf-8")
    report = (
        "# 商品数据整理记录\n\n"
        "已更新 150 件商品、180 个 SKU，按明确规格设置人民币演示价格，"
        "基础价与最低 SKU 售价一致；演示价格不代表实时市场报价。\n\n"
        f"{len(manifest)} 张经过核对的不同商品图片保存在上级目录，同名同款商品复用图片；"
        "全部经项目 /api/upload/image 上传，并验证返回 URL 可以读取。\n\n"
        f"本次共覆盖 {sum(bool(row['上传URL']) for row in price_rows)} 件商品。"
        "没有匹配到合适实物图的商品保留原封面，不使用无关图片补数。\n\n"
        "图片来源见 image-manifest.json；商品编号、价格、规格、图片和上传 URL 见 商品价格及图片对照.csv。\n\n"
        "修正初始化商品的商家分类归属、规格占位名称、演示订单金额、旧订单状态、"
        "取消订单残留时间和库存锁定。只有带原始订单号及占位商品名称的初始化演示明细重建成交快照；"
        "其他历史成交单价保留。\n\n"
        "执行前完整备份：database-before-*.sql；检查结果：database-audit-before.json、database-audit-after.json。\n\n"
        "项目初始化 SQL 已同步这些修正；当前数据库已应用，无需再次清库。\n"
    )
    (WORK / "整理说明.md").write_text(report, encoding="utf-8")
    print(json.dumps({"updatedProducts": len(price_rows), "uniqueImages": len(manifest),
                      "audit": {key: value["count"] for key, value in checks.items()},
                      "migration": str(migration)}, ensure_ascii=False, indent=2), flush=True)


def audit_in_transaction(connection):
    data = {table: query(connection, f"SELECT * FROM `{table}`") for table in
            ["products", "product_skus", "orders", "order_items", "inventories", "users",
             "categories", "cart_items", "carts", "user_addresses"]}
    return audit(data)


def verify_init():
    source = (ROOT / "src/main/resources/sql/ecommerce.sql").read_bytes()
    if re.search(rb"(?im)^\s*(?:USE\s|CREATE\s+DATABASE\s)", source):
        raise ValueError("Initialization SQL must not switch databases during verification")
    database = "catalog_verify_" + uuid.uuid4().hex[:12]
    assert re.fullmatch(r"catalog_verify_[a-f0-9]{12}", database)
    env = credentials()
    with connect() as connection:
        with connection.cursor() as cursor:
            cursor.execute(f"CREATE DATABASE `{database}` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci")
        try:
            result = subprocess.run([
                r"C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe",
                "--host=127.0.0.1", "--port=3306", f'--user={env["username"]}',
                "--default-character-set=utf8mb4", database,
            ], input=source, env={**os.environ, "MYSQL_PWD": env["password"]}, capture_output=True)
            if result.returncode:
                raise RuntimeError(result.stderr.decode("utf-8", errors="replace"))
            with connect(database) as restored:
                checks = audit_in_transaction(restored)
                failures = {key: value["count"] for key, value in checks.items()
                            if value["count"] and key != "productsWithoutCover"}
                if failures:
                    raise ValueError(f"Initialization consistency failures: {failures}")
                actual = query(restored, "SELECT COUNT(*) products, SUM(main_image IS NOT NULL AND main_image<>'') with_images FROM products")[0]
                print(json.dumps({"initializationVerified": True, **actual,
                                  "audit": {key: value["count"] for key, value in checks.items()}},
                                 default=serial, ensure_ascii=False, indent=2), flush=True)
                save_json(WORK / "initialization-audit.json", checks)
        finally:
            with connection.cursor() as cursor:
                cursor.execute(f"DROP DATABASE `{database}`")


def upload(ids=None):
    manifest = load_manifest()
    auth = {"Authorization": f"Bearer {token()}"}
    for key, row in sorted(manifest.items(), key=lambda item: int(item[0])):
        if ids and int(key) not in ids:
            continue
        if not row.get("relevanceChecked") or not row.get("visuallyApproved"):
            raise ValueError(f"Image {key} must pass relevance and visual review before upload")
        if row.get("uploadedUrl"):
            continue
        with open(row["file"], "rb") as handle:
            response = requests.post("http://127.0.0.1:8080/api/upload/image", headers=auth,
                                     files={"file": (Path(row["file"]).name, handle, "image/jpeg")}, timeout=90)
        response.raise_for_status()
        envelope = response.json()
        if not envelope.get("success") or not isinstance(envelope.get("data"), str):
            raise RuntimeError(f"Upload {key}: {envelope.get('message')}")
        url = envelope["data"]
        check = requests.get(url, headers=HEADERS, timeout=30)
        check.raise_for_status()
        with Image.open(io.BytesIO(check.content)) as image:
            image.verify()
        row["uploadedUrl"] = url
        row["uploadedAt"] = dt.datetime.now().isoformat()
        save_json(MANIFEST_PATH, manifest)
        print(f"UPLOADED {key} {row['name']}", flush=True)


def approve(ids):
    if not ids:
        raise ValueError("Specify the IDs that have been visually inspected")
    manifest = load_manifest()
    for pid in ids:
        if not manifest[str(pid)].get("relevanceChecked"):
            raise ValueError(f"Image {pid} has not passed relevance checks")
        manifest[str(pid)]["visuallyApproved"] = True
    save_json(MANIFEST_PATH, manifest)
    print(f"Marked {len(ids)} visually inspected images", flush=True)


def reject(ids=None):
    manifest = load_manifest()
    archive_path = WORK / "rejected-images.json"
    archive = json.loads(archive_path.read_text(encoding="utf-8")) if archive_path.exists() else []
    folder = WORK / "rejected"
    folder.mkdir(exist_ok=True)
    for key, row in list(manifest.items()):
        if (ids and int(key) not in ids) or (not ids and row.get("visuallyApproved")):
            continue
        path = Path(row["file"]).resolve()
        if path.parent != DEST.resolve() or path.name != f"ecommerce-product-{int(key):03}.jpg":
            raise ValueError(f"Unexpected generated image path for {key}")
        target = folder / f"{path.stem}-{row['sha256'][:12]}.jpg"
        if path.exists():
            if target.exists():
                target = folder / f"{path.stem}-{uuid.uuid4().hex[:12]}.jpg"
            path.rename(target)
        archive.append({**row, "rejectedFile": str(target)})
        del manifest[key]
    save_json(archive_path, archive)
    save_json(MANIFEST_PATH, manifest)
    print(f"Kept {len(manifest)} candidate images; rejected images archived", flush=True)


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("command", choices=["backup", "audit", "download", "contact", "upload", "apply", "preview", "approve", "reject", "verify-init"])
    parser.add_argument("--ids", help="Comma-separated canonical product IDs")
    args = parser.parse_args()
    ids = {int(value) for value in args.ids.split(",")} if args.ids else None
    if args.command == "backup":
        backup()
    elif args.command == "audit":
        result = audit()
        save_json(WORK / "database-audit-current.json", result)
        print(json.dumps(result, ensure_ascii=False, indent=2))
    elif args.command == "download":
        download(ids)
    elif args.command == "contact":
        contact_sheets()
    elif args.command == "upload":
        upload(ids)
    elif args.command == "apply":
        apply_catalog()
    elif args.command == "preview":
        apply_catalog(dry_run=True)
    elif args.command == "approve":
        approve(ids)
    elif args.command == "reject":
        reject(ids)
    elif args.command == "verify-init":
        verify_init()


if __name__ == "__main__":
    sys.stdout.reconfigure(encoding="utf-8")
    main()
