package com.example.es.utils;

import com.aliyun.sdk.service.oss2.OSSClient;
import com.aliyun.sdk.service.oss2.credentials.StaticCredentialsProvider;
import com.aliyun.sdk.service.oss2.exceptions.ServiceException;
import com.aliyun.sdk.service.oss2.models.*;
import com.aliyun.sdk.service.oss2.transport.BinaryData;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.aliyun.sdk.service.oss2.utils.IOUtils;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * 阿里云OSS工具类（完美版）
 * <p>
 * 设计原则：
 * 1. 使用OSS SDK V2（官方推荐，性能更优）
 * 2. 单例模式复用客户端（避免频繁创建/销毁的开销）
 * 3. 从配置文件读取凭证（配合环境变量占位符使用）
 * 4. Spring生命周期管理（@PostConstruct创建，@PreDestroy关闭）
 * 5. 完善的异常处理和日志记录
 * </p>
 * <p>
 * application.yml配置示例：
 * <pre>
 * aliyun:
 *   oss:
 *     endpoint: oss-cn-beijing.aliyuncs.com
 *     access-key-id: ${OSS_ACCESS_KEY_ID}
 *     access-key-secret: ${OSS_ACCESS_KEY_SECRET}
 *     bucket-name: your-bucket-name
 * </pre>
 * </p>
 *
 * @author 系统作者
 * @version 2.0
 * @since 2026-10-20
 */
@Component
public class AliOssUtils {

    /**
     * 日志记录器
     */
    private static final Logger log = LoggerFactory.getLogger(AliOssUtils.class);

    /**
     * OSS服务端点（从配置文件读取）
     * 例如：oss-cn-beijing.aliyuncs.com
     */
    @Value("${aliyun.oss.endpoint}")
    private String endpoint;

    /**
     * 阿里云AccessKey ID（从配置文件读取，配置中使用环境变量占位符）
     */
    @Value("${aliyun.oss.access-key-id}")
    private String accessKeyId;

    /**
     * 阿里云AccessKey Secret（从配置文件读取，配置中使用环境变量占位符）
     */
    @Value("${aliyun.oss.access-key-secret}")
    private String accessKeySecret;

    /**
     * OSS存储桶名称（从配置文件读取）
     * 例如：my-bucket-123
     */
    @Value("${aliyun.oss.bucket-name}")
    private String bucket;

    /**
     * OSS客户端实例（单例模式，全程复用）
     * <p>
     * 注意：客户端创建耗时较长（约1-2秒），禁止在业务方法中临时创建
     * 应该在应用启动时创建一次，由Spring容器管理生命周期
     * </p>
     */
    private OSSClient client;

    /**
     * 初始化OSS客户端
     * <p>
     * 在Spring容器启动时自动执行，使用配置文件中的endpoint和凭证构建客户端
     * 从endpoint中解析出region信息（如 oss-cn-beijing.aliyuncs.com → cn-beijing）
     * </p>
     *
     * @throws Exception 初始化失败时抛出异常
     */
    @PostConstruct
    public void init() {
        try {
            // 从endpoint中解析region，例如 oss-cn-beijing.aliyuncs.com → cn-beijing
            String region = endpoint.replace("oss-", "").replace(".aliyuncs.com", "");
            log.info("开始初始化OSS客户端，endpoint: {}，region: {}，存储桶: {}", endpoint, region, bucket);

            // 构建OSS客户端
            // 使用StaticCredentialsProvider，凭证从配置文件读取（配置中通过环境变量占位符注入）
            client = OSSClient.newBuilder()
                    .credentialsProvider(new StaticCredentialsProvider(accessKeyId, accessKeySecret))
                    .region(region)
                    .build();

            log.info("OSS客户端初始化成功");
        } catch (Exception e) {
            log.error("OSS客户端初始化失败", e);
            throw new RuntimeException("OSS客户端初始化失败，请检查配置文件中aliyun.oss相关配置", e);
        }
    }

    /**
     * 销毁OSS客户端
     * <p>
     * 在Spring容器关闭时自动执行，确保资源正确释放
     * 关闭客户端时的异常不会影响应用正常停止
     * </p>
     */
    @PreDestroy
    public void destroy() {
        if (client != null) {
            try {
                log.info("开始关闭OSS客户端");
                client.close();
                log.info("OSS客户端已关闭");
            } catch (Exception e) {
                // 关闭客户端时的异常不影响应用停止，仅记录警告日志
                log.warn("关闭OSS客户端时发生异常", e);
            }
        }
    }

    /**
     * 上传文件到OSS
     * <p>
     * 功能特点：
     * 1. 使用UUID生成唯一文件名，防止文件覆盖
     * 2. 保留原文件扩展名，便于识别文件类型
     * 3. 返回外网访问URL，可直接用于前端展示
     * 4. 完善的异常处理，区分IO异常和OSS服务异常
     * </p>
     * <p>
     * 使用示例：
     * <pre>
     * {@code
     * @Autowired
     * private AliOssUtils aliOssUtils;
     *
     * public String uploadAvatar(MultipartFile file) {
     *     String url = aliOssUtils.upload(file);
     *     // url格式: https://bucket.oss-region.aliyuncs.com/uuid.ext
     *     return url;
     * }
     * }
     * </pre>
     * </p>
     *
     * @param file 前端上传的文件对象（MultipartFile类型）
     * @return 文件的外网访问URL，格式：https://{bucket}.oss-{region}.aliyuncs.com/{key}
     * @throws RuntimeException 上传失败时抛出，包含详细的错误信息
     */
    public String upload(MultipartFile file) {
        // 参数校验
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("上传文件不能为空");
        }

        // 生成唯一文件名：UUID + 原扩展名
        String originalFilename = file.getOriginalFilename();
        String ext = StringUtils.getFilenameExtension(originalFilename);
        String key = UUID.randomUUID() + (ext == null ? "" : "." + ext);

        log.info("开始上传文件到OSS，原始文件名: {}，OSS对象键: {}，文件大小: {} bytes",
                originalFilename, key, file.getSize());

        try {
            // 构建上传请求
            // 使用BinaryData.fromStream从输入流读取数据，适合大文件上传
            PutObjectRequest request = PutObjectRequest.newBuilder()
                    .bucket(bucket)                           // 存储桶名称
                    .key(key)                                 // 对象键（文件路径）
                    .body(BinaryData.fromStream(
                            file.getInputStream(),            // 文件输入流
                            file.getSize()                    // 文件大小，用于优化上传
                    ))
                    .build();

            // 执行上传操作
            client.putObject(request);

            log.info("文件上传成功: {}", key);

        } catch (IOException e) {
            // IO异常：读取文件输入流失败
            log.error("读取上传文件失败: {}", originalFilename, e);
            throw new RuntimeException("读取上传文件失败", e);

        } catch (Exception e) {
            // OSS服务异常：尝试解析为ServiceException获取详细信息
            ServiceException se = ServiceException.asCause(e);
            if (se != null) {
                // OSS服务异常，记录请求ID和错误码，便于排查问题
                log.error("OSS上传失败，请求ID: {}，错误码: {}，错误信息: {}",
                        se.requestId(), se.errorCode(), se.errorMessage(), e);
                throw new RuntimeException(
                        String.format("OSS上传失败: requestId=%s, errorCode=%s, errorMessage=%s",
                                se.requestId(), se.errorCode(), se.errorMessage()),
                        se);
            }
            // 其他未知异常
            log.error("OSS上传失败: {}", originalFilename, e);
            throw new RuntimeException("OSS上传失败", e);
        }

        // 拼接并返回外网访问URL
        // 格式：https://{bucket}.{endpoint}/{key}
        String url = "https://" + bucket + "." + endpoint + "/" + key;
        log.debug("生成文件访问URL: {}", url);
        return url;
    }

    /**
     * 从OSS下载文件
     * <p>
     * 功能特点：
     * 1. 根据对象键下载文件，返回字节数组
     * 2. 适用于小文件下载（大文件建议使用流式下载）
     * 3. 完善的异常处理，区分OSS服务异常和其他异常
     * </p>
     * <p>
     * 使用示例：
     * <pre>
     * {@code
     * @Autowired
     * private AliOssUtils aliOssUtils;
     *
     * public byte[] downloadFile(String key) {
     *     byte[] data = aliOssUtils.download(key);
     *     // 处理文件数据...
     *     return data;
     * }
     * }
     * </pre>
     * </p>
     *
     * @param key OSS对象键（上传时生成的UUID文件名）
     * @return 文件内容的字节数组
     * @throws RuntimeException 下载失败时抛出，包含详细的错误信息
     */
    public byte[] download(String key) {
        // 参数校验
        if (!StringUtils.hasText(key)) {
            throw new IllegalArgumentException("OSS对象键不能为空");
        }

        log.info("开始从OSS下载文件，对象键: {}", key);

        try {
            // 构建下载请求
            GetObjectRequest request = GetObjectRequest.newBuilder()
                    .bucket(bucket)
                    .key(key)
                    .build();

            // 执行下载操作
            GetObjectResult result = client.getObject(request);

            // 读取文件内容到字节数组
            byte[] data = IOUtils.toByteArray(result.body());
            log.info("文件下载成功，对象键: {}，文件大小: {} bytes", key, data.length);
            return data;

        } catch (Exception e) {
            // OSS服务异常：尝试解析为ServiceException获取详细信息
            ServiceException se = ServiceException.asCause(e);
            if (se != null) {
                log.error("OSS下载失败，请求ID: {}，错误码: {}，错误信息: {}",
                        se.requestId(), se.errorCode(), se.errorMessage(), e);
                throw new RuntimeException(
                        String.format("OSS下载失败: requestId=%s, errorCode=%s, errorMessage=%s",
                                se.requestId(), se.errorCode(), se.errorMessage()),
                        se);
            }
            // 其他未知异常
            log.error("OSS下载失败，对象键: {}", key, e);
            throw new RuntimeException("OSS下载失败", e);
        }
    }

    /**
     * 从OSS删除文件
     * <p>
     * 功能特点：
     * 1. 根据对象键删除指定文件
     * 2. 删除操作不可恢复，请谨慎使用
     * 3. 文件不存在时不会抛出异常（幂等性）
     * </p>
     * <p>
     * 使用示例：
     * <pre>
     * {@code
     * @Autowired
     * private AliOssUtils aliOssUtils;
     *
     * public void deleteFile(String key) {
     *     aliOssUtils.delete(key);
     *     // 文件已删除...
     * }
     * }
     * </pre>
     * </p>
     *
     * @param key OSS对象键（上传时生成的UUID文件名）
     * @throws RuntimeException 删除失败时抛出，包含详细的错误信息
     */
    public void delete(String key) {
        // 参数校验
        if (!StringUtils.hasText(key)) {
            throw new IllegalArgumentException("OSS对象键不能为空");
        }

        log.info("开始从OSS删除文件，对象键: {}", key);

        try {
            // 构建删除请求
            DeleteObjectRequest request = DeleteObjectRequest.newBuilder()
                    .bucket(bucket)
                    .key(key)
                    .build();

            // 执行删除操作
            client.deleteObject(request);

            log.info("文件删除成功: {}", key);

        } catch (Exception e) {
            // OSS服务异常：尝试解析为ServiceException获取详细信息
            ServiceException se = ServiceException.asCause(e);
            if (se != null) {
                log.error("OSS删除失败，请求ID: {}，错误码: {}，错误信息: {}",
                        se.requestId(), se.errorCode(), se.errorMessage(), e);
                throw new RuntimeException(
                        String.format("OSS删除失败: requestId=%s, errorCode=%s, errorMessage=%s",
                                se.requestId(), se.errorCode(), se.errorMessage()),
                        se);
            }
            // 其他未知异常
            log.error("OSS删除失败，对象键: {}", key, e);
            throw new RuntimeException("OSS删除失败", e);
        }
    }

    /**
     * 检查文件是否存在
     * <p>
     * 功能特点：
     * 1. 检查指定对象键的文件是否存在于OSS中
     * 2. 返回布尔值，不抛出异常
     * </p>
     * <p>
     * 使用示例：
     * <pre>
     * {@code
     * @Autowired
     * private AliOssUtils aliOssUtils;
     *
     * public boolean fileExists(String key) {
     *     return aliOssUtils.exists(key);
     * }
     * }
     * </pre>
     * </p>
     *
     * @param key OSS对象键（上传时生成的UUID文件名）
     * @return 文件存在返回true，不存在返回false
     */
    public boolean exists(String key) {
        // 参数校验
        if (!StringUtils.hasText(key)) {
            throw new IllegalArgumentException("OSS对象键不能为空");
        }

        try {
            // 构建查询请求
            HeadObjectRequest request = HeadObjectRequest.newBuilder()
                    .bucket(bucket)
                    .key(key)
                    .build();

            // 执行查询操作
            client.headObject(request);

            log.debug("文件存在: {}", key);
            return true;

        } catch (ServiceException e) {
            // 404表示文件不存在，属于正常情况
            if ("NoSuchKey".equals(e.errorCode())) {
                log.debug("文件不存在: {}", key);
                return false;
            }
            // 其他服务异常
            log.error("检查文件存在时发生异常，对象键: {}，错误码: {}", key, e.errorCode(), e);
            throw new RuntimeException("检查文件存在时发生异常", e);

        } catch (Exception e) {
            log.error("检查文件存在时发生异常，对象键: {}", key, e);
            throw new RuntimeException("检查文件存在时发生异常", e);
        }
    }

    /**
     * 获取文件元信息
     * <p>
     * 功能特点：
     * 1. 获取文件的元数据信息（大小、类型、修改时间等）
     * 2. 不下载文件内容，仅获取元信息
     * </p>
     * <p>
     * 使用示例：
     * <pre>
     * {@code
     * @Autowired
     * private AliOssUtils aliOssUtils;
     *
     * public void printFileInfo(String key) {
     *     HeadObjectResult info = aliOssUtils.getFileInfo(key);
     *     System.out.println("文件大小: " + info.contentLength());
     *     System.out.println("文件类型: " + info.contentType());
     * }
     * }
     * </pre>
     * </p>
     *
     * @param key OSS对象键（上传时生成的UUID文件名）
     * @return 文件元信息结果对象
     * @throws RuntimeException 获取失败时抛出，包含详细的错误信息
     */
    public HeadObjectResult getFileInfo(String key) {
        // 参数校验
        if (!StringUtils.hasText(key)) {
            throw new IllegalArgumentException("OSS对象键不能为空");
        }

        log.info("开始获取文件元信息，对象键: {}", key);

        try {
            // 构建查询请求
            HeadObjectRequest request = HeadObjectRequest.newBuilder()
                    .bucket(bucket)
                    .key(key)
                    .build();

            // 执行查询操作
            HeadObjectResult result = client.headObject(request);

            log.info("获取文件元信息成功，对象键: {}，大小: {} bytes",
                    key, result.contentLength());
            return result;

        } catch (Exception e) {
            // OSS服务异常：尝试解析为ServiceException获取详细信息
            ServiceException se = ServiceException.asCause(e);
            if (se != null) {
                log.error("获取文件元信息失败，请求ID: {}，错误码: {}，错误信息: {}",
                        se.requestId(), se.errorCode(), se.errorMessage(), e);
                throw new RuntimeException(
                        String.format("获取文件元信息失败: requestId=%s, errorCode=%s, errorMessage=%s",
                                se.requestId(), se.errorCode(), se.errorMessage()),
                        se);
            }
            // 其他未知异常
            log.error("获取文件元信息失败，对象键: {}", key, e);
            throw new RuntimeException("获取文件元信息失败", e);
        }
    }

    /**
     * 遍历当前账号下所有Bucket（连通性自测）
     * <p>
     * 功能特点：
     * 1. 使用分页器遍历所有Bucket
     * 2. 可用于测试OSS客户端连通性
     * 3. 返回Bucket名称列表
     * </p>
     * <p>
     * 使用示例：
     * <pre>
     * {@code
     * @Autowired
     * private AliOssUtils aliOssUtils;
     *
     * public void testConnection() {
     *     List<String> buckets = aliOssUtils.listBuckets();
     *     System.out.println("当前账号下的Bucket数量: " + buckets.size());
     *     buckets.forEach(System.out::println);
     * }
     * }
     * </pre>
     * </p>
     *
     * @return Bucket名称列表
     * @throws RuntimeException 遍历失败时抛出，包含详细的错误信息
     */
    public java.util.List<String> listBuckets() {
        log.info("开始遍历OSS Bucket列表");

        java.util.List<String> names = new java.util.ArrayList<>();

        try {
            // 使用分页器遍历所有Bucket
            // 官方同步示例逻辑，适用于连通性自测
            com.aliyun.sdk.service.oss2.paginator.ListBucketsIterable paginator =
                    client.listBucketsPaginator(
                            ListBucketsRequest.newBuilder().build()
                    );

            // 遍历分页结果
            for (ListBucketsResult result : paginator) {
                for (BucketSummary info : result.buckets()) {
                    names.add(info.name());
                }
            }

            log.info("遍历完成，共发现 {} 个Bucket", names.size());
            return names;

        } catch (Exception e) {
            // OSS服务异常：尝试解析为ServiceException获取详细信息
            ServiceException se = ServiceException.asCause(e);
            if (se != null) {
                log.error("遍历Bucket失败，请求ID: {}，错误码: {}，错误信息: {}",
                        se.requestId(), se.errorCode(), se.errorMessage(), e);
                throw new RuntimeException(
                        String.format("遍历Bucket失败: requestId=%s, errorCode=%s, errorMessage=%s",
                                se.requestId(), se.errorCode(), se.errorMessage()),
                        se);
            }
            // 其他未知异常
            log.error("遍历Bucket失败", e);
            throw new RuntimeException("遍历Bucket失败", e);
        }
    }
}
