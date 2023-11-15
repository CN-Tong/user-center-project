package com.tong.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tong.common.Code;
import com.tong.exception.BusinessException;
import com.tong.model.domain.User;
import com.tong.service.UserService;
import com.tong.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.tong.constant.UserConstant.USER_LOGIN_STATE;

/**
 * @author Tong
 * @description 针对表【user(用户)】的数据库操作Service实现
 * @createDate 2023-11-06 17:09:32
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Autowired
    UserMapper userMapper;

    /**
     * 盐值：混淆密码
     */
    private static final String SALT = "tong";

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword, String nkuCode) {
        // 2.校验账户和密码
        // 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(Code.PARAMS_ERROR, "必填项为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(Code.PARAMS_ERROR, "用户账号不得小于4位");
        }
        if (userPassword.length() < 8) {
            throw new BusinessException(Code.PARAMS_ERROR, "用户密码不得小于8位");
        }
        if(nkuCode.length() > 10 || nkuCode.length() < 7){
            throw new BusinessException(Code.PARAMS_ERROR, "NKU学号格式不正确");
        }
        // 账号不能包含特殊字符
        String regEx = "[0-9a-zA-z]+";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(userAccount);
        if (!matcher.matches()) {
            throw new BusinessException(Code.PARAMS_ERROR, "用户账号只能包含字母和数字");
        }
        // 密码和校验密码不能相同
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(Code.PARAMS_ERROR, "密码和校验密码不能相同");
        }
        // 账号不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(Code.PARAMS_ERROR, "用户账号已存在");
        }
        // NKU学号不能重复
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("nkuCode", nkuCode);
        count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(Code.PARAMS_ERROR, "NKU学号已注册");
        }

        // 3.加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        // 4.插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setNkuCode(nkuCode);
        boolean saveResult = this.save(user);
        if (!saveResult) {
            throw new BusinessException(Code.PARAMS_ERROR, "添加用户失败");
        }
        return user.getId();
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 1.校验用户账户和密码是否合法
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(Code.PARAMS_ERROR, "必填项为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(Code.PARAMS_ERROR, "用户账号不得小于4位");
        }
        if (userPassword.length() < 8) {
            throw new BusinessException(Code.PARAMS_ERROR, "用户密码不得小于8位");
        }
        // 账户不能包含特殊字符
        String regEx = "[0-9a-zA-z]+";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(userAccount);
        if (!matcher.matches()) {
            throw new BusinessException(Code.PARAMS_ERROR, "用户账号只能包含字母和数字");
        }

        // 2.加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        // 查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);
        User user = userMapper.selectOne(queryWrapper);
        // 用户不存在
        if (user == null) {
            log.info("user login failed, userAccount cannot match userPassword");
            throw new BusinessException(Code.PARAMS_ERROR, "用户账户和密码不匹配");
        }

        // 3.用户信息脱敏
        User safetyUser = getSafetyUser(user);

        // 4.记录用户的登录状态
        request.getSession().setAttribute(USER_LOGIN_STATE, safetyUser);

        return safetyUser;
    }

    @Override
    public int userLogout(HttpServletRequest request) {
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;
    }

    @Override
    public List<User> searchUsers(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)) {
            queryWrapper.like("username", username);
        }
        List<User> userList = userMapper.selectList(queryWrapper);
        //用户脱敏
        return userList.stream().map(user -> getSafetyUser(user)).collect(Collectors.toList());
    }

    @Override
    public boolean deleteUser(long id) {
        return userMapper.deleteById(id) > 0;
    }

    @Override
    public User getSafetyUser(User originUser){
        if(originUser == null){
            return null;
        }
        User safetyUser = new User();
        safetyUser.setId(originUser.getId());
        safetyUser.setUsername(originUser.getUsername());
        safetyUser.setUserAccount(originUser.getUserAccount());
        safetyUser.setAvatarUrl(originUser.getAvatarUrl());
        safetyUser.setGender(originUser.getGender());
        safetyUser.setPhone(originUser.getPhone());
        safetyUser.setEmail(originUser.getEmail());
        safetyUser.setUserStatus(originUser.getUserStatus());
        safetyUser.setCreateTime(originUser.getCreateTime());
        safetyUser.setUserRole(originUser.getUserRole());
        safetyUser.setNkuCode(originUser.getNkuCode());
        return safetyUser;
    }
}




