package com.tong.service;

import com.tong.common.BaseResponse;
import com.tong.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户服务
 *
 * @author Tong
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     *
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @param nkuCode NKU学号
     * @return 新用户id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword, String nkuCode);

    /**
     * 用户登录
     *
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     * @return 脱敏后的用户信息
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 用户注销
     *
     * @param request
     */
    int userLogout(HttpServletRequest request);

    /**
     * 按用户名查询用户
     *
     * @param username 用户名
     * @return 用户信息列表
     */
    List<User> searchUsers(String username);

    /**
     * 按id删除用户
     *
     * @param id 用户id
     * @return 是否删除成功
     */
    boolean deleteUser(long id);

    /**
     * 用户脱敏
     *
     * @param originUser 原始用户信息
     * @return 脱敏后的用户信息
     */
    User getSafetyUser(User originUser);

}
