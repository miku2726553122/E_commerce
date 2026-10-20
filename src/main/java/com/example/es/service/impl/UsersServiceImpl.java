package com.example.es.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.example.es.entity.Users;
import com.example.es.enums.ResponseCodeEnum;
import com.example.es.service.UsersService;
import com.example.es.mapper.UsersMapper;
import com.example.es.utils.JwtUtils;
import com.example.es.utils.PasswordUtil;
import com.example.es.utils.Response;
import com.example.es.vo.UserDetail;
import com.example.es.vo.admin.AddMerchantsReqVO;
import com.example.es.vo.admin.AddMerchantsRspVO;
import com.example.es.vo.admin.FindAllRoleReqVO;
import com.example.es.vo.admin.FindByIdRspVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.example.es.vo.merchant.shopproduct.MerchantProfileRspVO;
import com.example.es.vo.merchant.shopproduct.UpdateMerchantProfileReqVO;

/**
* @author 86159
* @description 针对表【users(用户表)】的数据库操作Service实现
* @createDate 2026-10-19 10:21:26
*/
@Slf4j
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users>
    implements UsersService{

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private JwtUtils jwtUtils;


    /**
     * 查询所有角色
     * @param reqVO
     * @return
     */
    @Override
    public Response selectAllRole(FindAllRoleReqVO reqVO) {
        Page<Users> page = usersMapper.selectPageList(reqVO.getPageNum(), reqVO.getPageSize(), reqVO.getRole(), reqVO.getStatus(), reqVO.getKeyword());
        return Response.success(page);
    }

    /**
     * 根据id获取用户信息
     * @param id
     * @return
     */
    @Override
    public Response selectById(Long id) {
        Users users = usersMapper.selectById(id);
        FindByIdRspVO findByIdRspVO = FindByIdRspVO.builder()
                .id(users.getId())
                .username(users.getUsername())
                .nickname(users.getNickname())
                .role(users.getRole())
                .status(users.getStatus())
                .build();
        return Response.success(findByIdRspVO);
    }

    /**
     * 添加商户
     * @param addMerchantsReqVO
     * @return
     */
    @Override
    public Response addMerchant(AddMerchantsReqVO addMerchantsReqVO) {
        // 加密密码
        String encodedPassword = PasswordUtil.encode(addMerchantsReqVO.getPassword());

        Users users = Users.builder()
                .username(addMerchantsReqVO.getUsername())
                .passwordHash(encodedPassword)
                .nickname(addMerchantsReqVO.getNickname())
                .shopName(addMerchantsReqVO.getShopName())
                .role("MERCHANT")
                .status(1)
                .build();
        usersMapper.insert(users);
        AddMerchantsRspVO addMerchantsRspVO = AddMerchantsRspVO.builder()
                .id(users.getId())
                .username(users.getUsername())
                .nickname(users.getNickname())
                .shopName(users.getShopName() == null ? null : users.getShopName())
                .build();

        return Response.success(addMerchantsRspVO);
    }

    /**
     * 启用禁用用户
     * @param id
     * @param status
     * @return
     */
    @Override
    public Response enableOrDisableUser(Long id, Integer status) {

        Users findUser = usersMapper.selectById(id);

        if(findUser.getStatus().equals(status) || !(status.equals(0)||status.equals(1))){
            return Response.fail(ResponseCodeEnum.PARAM_ERROR);
        }

        Users users = Users.builder()
                .id(id)
                .status(status)
                .build();
        this.updateById(users);
        return Response.success(null);
    }

    /**
     * 重置密码
     * @param id
     * @param newPassword
     * @return
     */
    @Override
    public Response resetPassword(Long id, String newPassword) {
        Users user = usersMapper.selectById(id);

        if (user == null){
            return Response.fail(ResponseCodeEnum.USER_NOT_FOUND);
        }

        if (newPassword == null || newPassword.isEmpty())
            return Response.fail(ResponseCodeEnum.PARAM_ERROR);

        if (newPassword.length() < 6 || newPassword.length() > 20)
            return Response.fail(ResponseCodeEnum.PARAM_ERROR);

        if (user.getPasswordHash() != null
                && PasswordUtil.matches(newPassword, user.getPasswordHash()))
            return Response.fail(ResponseCodeEnum.PASSWORD_SAME);

        String encodedPassword = PasswordUtil.encode(newPassword);
        user.setPasswordHash(encodedPassword);
        usersMapper.updateById(user);

        return Response.success(null);
    }

    @Override
    public Response getMerchantProfile(Long merchantId) {
        Users user = usersMapper.selectById(merchantId);
        if (user == null) {
            return Response.fail(ResponseCodeEnum.USER_NOT_FOUND);
        }
        if (!"MERCHANT".equals(user.getRole())) {
            return Response.fail(ResponseCodeEnum.FORBIDDEN);
        }

        MerchantProfileRspVO rspVO = MerchantProfileRspVO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .shopName(user.getShopName())
                .shopLogo(user.getShopLogo())
                .build();
        return Response.success(rspVO);
    }

    @Override
    public Response updateMerchantProfile(Long merchantId, UpdateMerchantProfileReqVO reqVO) {
        Users user = usersMapper.selectById(merchantId);
        if (user == null) {
            return Response.fail(ResponseCodeEnum.USER_NOT_FOUND);
        }
        if (!"MERCHANT".equals(user.getRole())) {
            return Response.fail(ResponseCodeEnum.FORBIDDEN);
        }
        if (reqVO == null) {
            return Response.fail(ResponseCodeEnum.PARAM_ERROR);
        }

        Users update = Users.builder()
                .id(merchantId)
                .shopName(reqVO.getShopName())
                .shopLogo(reqVO.getShopLogo())
                .nickname(reqVO.getNickname())
                .build();
        boolean updated = this.updateById(update);
        return Response.success(updated);
    }

    /**
     * 删除用户信息
     * @param id
     * @return
     */
    @Override
    public Response deleteUserInfo(Long id) {
        Users findUser = usersMapper.selectById(id);

        if (findUser == null)
            return Response.fail(ResponseCodeEnum.USER_NOT_FOUND);

        usersMapper.deleteById(id);
        return Response.success();
    }

    @Override
    public ResponseEntity<String> login(UserDetail userDetail) {
        // 1. 根据用户名查用户
        Users user = usersMapper.findByUsername(userDetail.getUsername());
        if (user == null) {
            log.warn("登录失败：用户 [{}] 不存在", userDetail.getUsername());
            return ResponseEntity.status(401).build();
        }

        // 2. 账号是否启用
        if (user.getStatus() == null || user.getStatus() != 1) {
            log.warn("登录失败：账号 [{}] 已被禁用", userDetail.getUsername());
            return ResponseEntity.status(403).build();
        }

        // 3. 校验密码（BCrypt 不能用 equals 直接比对密文）
        if (!PasswordUtil.matches(userDetail.getPassword(), user.getPasswordHash())) {
            log.warn("登录失败：用户 [{}] 密码错误", userDetail.getUsername());
            return ResponseEntity.status(401).build();
        }

        // 4. 生成 JWT
        String token = jwtUtils.generateToken(user);
        return ResponseEntity.ok(token);
    }
}




