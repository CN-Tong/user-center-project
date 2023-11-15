package com.tong.utils;

import com.tong.common.BaseResponse;
import com.tong.common.Code;

/**
 * 返回工具类
 *
 * @author Tong
 */
public class ResultUtils {

    public static <T> BaseResponse<T> success(T data){
        return new BaseResponse<T>(Code.SUCCESS, data);
    }

    public static BaseResponse error(Code code){
        return new BaseResponse(code);
    }

    public static BaseResponse error(Code code, String description){
        return new BaseResponse(code.getCode(), code.getMessage(), description);
    }

    public static BaseResponse error(Code code, String message, String description){
        return new BaseResponse(code.getCode(), message, description);
    }

    public static BaseResponse error(int code, String message, String description){
        return new BaseResponse(code, message, description);
    }
}
