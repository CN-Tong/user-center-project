package com.tong.service;

import java.util.Date;

import com.tong.model.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 用户服务测试
 *
 * @author Tong
 */
@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    void userRegister() {
        String userAccount = "tong";
        String userPassword = "";
        String checkPassword = "123456789";
        String nkuCode = "1";
        long result = userService.userRegister(userAccount, userPassword, checkPassword, nkuCode);
        Assertions.assertEquals(-1, result);

        userAccount = "to";
        userPassword = "123456789";
        checkPassword = "123456789";
        result = userService.userRegister(userAccount, userPassword, checkPassword, nkuCode);
        Assertions.assertEquals(-1, result);

        userAccount = "tong";
        userPassword = "123456";
        checkPassword = "123456";
        result = userService.userRegister(userAccount, userPassword, checkPassword, nkuCode);
        Assertions.assertEquals(-1, result);

        userAccount = "to ng";
        userPassword = "123456789";
        checkPassword = "123456789";
        result = userService.userRegister(userAccount, userPassword, checkPassword, nkuCode);
        Assertions.assertEquals(-1, result);

        userAccount = "tong";
        userPassword = "123456789";
        checkPassword = "123456";
        result = userService.userRegister(userAccount, userPassword, checkPassword, nkuCode);
        Assertions.assertEquals(-1, result);
    }
}