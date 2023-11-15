package com.tong.model.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求体
 *
 * @author Tong
 */
@Data
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = 4576294360761831384L;

    private String userAccount;

    private String userPassword;
}
