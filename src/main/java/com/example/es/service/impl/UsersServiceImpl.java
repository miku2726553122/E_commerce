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
import com.example.es.utils.SecurityUtils;
import com.example.es.vo.UserDetail;
import com.example.es.vo.auth.LoginReqVO;
import com.example.es.vo.admin.AddMerchantsReqVO;
import com.example.es.vo.admin.AddMerchantsRspVO;
import com.example.es.vo.admin.FindAllRoleReqVO;
import com.example.es.vo.admin.FindByIdRspVO;
import com.example.es.vo.user.FindUserInfoRspVO;
import com.example.es.vo.user.UpdatePasswordReqVO;
import com.example.es.vo.user.UpdateUserInfoReqVO;
import com.example.es.vo.user.UserLoginRspVO;
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
        // 校验密码强度
        if (!PasswordUtil.validateStrength(addMerchantsReqVO.getPassword())) {
            return Response.fail(ResponseCodeEnum.PASSWORD_WEAK);
        }

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

        // 校验密码强度
        if (!PasswordUtil.validateStrength(newPassword))
            return Response.fail(ResponseCodeEnum.PASSWORD_WEAK);

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

    /**
     * 登录
     * @param loginReqVO
     * @return
     */
    @Override
    public Response login(LoginReqVO loginReqVO) {
        // 1. 根据用户名查用户
        Users user = usersMapper.findByUsername(loginReqVO.getUsername());
        if (user == null) {
            log.warn("登录失败：用户 [{}] 不存在", loginReqVO.getUsername());
            return Response.fail(ResponseCodeEnum.USER_NOT_FOUND);
        }

        // 2. 账号是否启用
        if (user.getStatus() == null || user.getStatus() != 1) {
            log.warn("登录失败：账号 [{}] 已被禁用", loginReqVO.getUsername());
            return Response.fail(ResponseCodeEnum.ACCOUNT_DISABLED);
        }

        // 3. 校验密码（BCrypt 不能用 equals 直接比对密文）
        if (!PasswordUtil.matches(loginReqVO.getPassword(), user.getPasswordHash())) {
            log.warn("登录失败：用户 [{}] 密码错误", loginReqVO.getUsername());
            return Response.fail(ResponseCodeEnum.USERNAME_PASSWORD_ERROR);
        }

        // 4. 生成 JWT
        String token = jwtUtils.generateToken(user);

        //5.查询角色
        String role = user.getRole();

        UserLoginRspVO userLoginRspVO = UserLoginRspVO.builder()
                .token(token)
                .userId(user.getId())
                .role(role)
                .build();

        return Response.success(userLoginRspVO);
    }

    /**
     * 注册
     * @param userDetail
     * @return
     */
    @Override
    public ResponseEntity<String> register(UserDetail userDetail) {
        log.info("注册：{}", userDetail.getUsername());

        // 校验密码强度
        if (!PasswordUtil.validateStrength(userDetail.getPassword())) {
            return ResponseEntity.status(400).body("密码强度不足：长度6-20位，至少包含大写字母、小写字母、数字中的两种");
        }

        //1.用户是否存在
        Users user = usersMapper.findByUsername(userDetail.getUsername());
        if (user != null) {
            log.warn("注册失败：用户 [{}] 已存在", userDetail.getUsername());
            return ResponseEntity.status(400).build();
        }

        //2.密码加密
        String encodedPassword = PasswordUtil.encode(userDetail.getPassword());
        //3.保存用户
        Users newUser = Users.builder()
                .username(userDetail.getUsername())
                .passwordHash(encodedPassword)
                .nickname(userDetail.getNickname() != null ? userDetail.getNickname() : "")
                .phone(userDetail.getPhone() != null ? userDetail.getPhone() : "")
                .build();
        usersMapper.insert(newUser);
        //4.生成JWT
        String token = jwtUtils.generateToken(newUser);
        return ResponseEntity.ok(token);
    }

    /**
     * 查找用户信息
     * @return
     */
    @Override
    public Response FindUserInfo() {
        String username = SecurityUtils.getCurrentUsername();
        //如果为空返回未登录
        if (username == null)
            return Response.fail(ResponseCodeEnum.NOT_LOGGED_IN);

        Users users = usersMapper.findByUsername(username);

        // 用户不存在时返回错误提示
        if (users == null) {
            return Response.fail(ResponseCodeEnum.USER_NOT_FOUND);
        }

        //DO转VO
        FindUserInfoRspVO vos = FindUserInfoRspVO.builder()
                .id(users.getId())
                .username(users.getUsername())
                .nickname(users.getNickname())
                .phone(users.getPhone())
                .build();

        return Response.success(vos);

    }

    /**
     * 更新用户信息
     * @param updateUserInfoReqVO
     * @return
     */

    @Override
    public Response UpdateUserInfo(UpdateUserInfoReqVO updateUserInfoReqVO) {
        //1.获取当前用户username
        String username = SecurityUtils.getCurrentUsername();
        if (username == null)
            return Response.fail(ResponseCodeEnum.NOT_LOGGED_IN);

        boolean result = usersMapper.updateUserInfo(updateUserInfoReqVO, username);

        return Response.success(result);
    }

    @Override
    public Response UpdatePassword(UpdatePasswordReqVO updatePasswordReqVO) {
        String username = SecurityUtils.getCurrentUsername();
        if (username == null)
            return Response.fail(ResponseCodeEnum.NOT_LOGGED_IN);


        Users user = usersMapper.findByUsername(username);
        if (user == null) {
            return Response.fail(ResponseCodeEnum.USER_NOT_FOUND);
        }
        // 验证旧密码
        if (!PasswordUtil.matches(updatePasswordReqVO.getOldPassword(), user.getPasswordHash())) {
            return Response.fail(ResponseCodeEnum.USERNAME_PASSWORD_ERROR);
        }

        //新旧密码是否相同
        if(updatePasswordReqVO.getNewPassword().equals(updatePasswordReqVO.getOldPassword()))
            return Response.fail(ResponseCodeEnum.PASSWORD_SAME);

        // 校验密码强度
        if (!PasswordUtil.validateStrength(updatePasswordReqVO.getNewPassword()))
            return Response.fail(ResponseCodeEnum.PASSWORD_WEAK);

        boolean result = usersMapper.updatePassword(updatePasswordReqVO, username);
        return Response.success(result);
    }
}




