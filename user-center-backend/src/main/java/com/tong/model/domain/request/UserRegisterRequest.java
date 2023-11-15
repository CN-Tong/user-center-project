package com.tong.model.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求体
 *
 * @author Tong
 */
@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = -6496947964780712655L;

    private String userAccount;

    private String userPassword;

    private String checkPassword;

    private String nkuCode;
}
