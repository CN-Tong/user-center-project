package com.tong.exception;

import com.tong.common.Code;
import lombok.Getter;

/**
 * 自定义异常类
 *
 * @author Tong
 */
@Getter
public class BusinessException extends RuntimeException{
    private static final long serialVersionUID = 8687561842132493802L;

    private final int code;

    private final String description;

    public BusinessException(String message, int code, String description) {
        super(message);
        this.code = code;
        this.description = description;
    }

    public BusinessException(Code code){
        this(code.getMessage(), code.getCode(), code.getDescription());
    }

    public BusinessException(Code code, String description){
        this(code.getMessage(), code.getCode(), description);
    }
}
