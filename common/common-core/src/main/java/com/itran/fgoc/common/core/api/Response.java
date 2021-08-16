package com.itran.fgoc.common.core.api;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author: cocoyang
 * @create: 2020-08-03 14:53
 * @description: 通用返回对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response<T> {

    @ApiModelProperty(value = "状态码")
    private long code;


    @ApiModelProperty(value = "响应信息")
    private String message;


    @ApiModelProperty(value = "响应数据")
    private T data;

    /**
     * 是否请求成功
     *
     * @return
     */
    public boolean isSuccess(){
        return this.code == 200;
    }

    /**
     * 成功返回空结果
     */
    public static Response success() {
        return new Response(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), null);
    }

    /**
     * 成功返回结果
     *
     * @param data 获取的数据
     */
    public static <T> Response<T> success(T data) {
        return new Response<T>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }

    /**
     * 成功返回结果
     *
     * @param data    获取的数据
     * @param message 提示信息
     */
    public static <T> Response<T> success(T data, String message) {
        return new Response<T>(ResultCode.SUCCESS.getCode(), message, data);
    }

    /**
     * 失败返回结果
     *
     * @param errorCode 错误码
     */
    public static <T> Response<T> failed(IErrorCode errorCode) {
        return new Response<T>(errorCode.getCode(), errorCode.getMessage(), null);
    }

    /**
     * 失败返回结果
     *
     * @param errorCode 错误码
     * @param message   错误信息
     */
    public static <T> Response<T> failed(IErrorCode errorCode, String message) {
        return new Response<T>(errorCode.getCode(), message, null);
    }

    /**
     * 失败返回结果
     *
     * @param message 提示信息
     */
    public static <T> Response<T> failed(String message) {
        return new Response<T>(ResultCode.FAILED.getCode(), message, null);
    }

    /**
     * 失败返回结果
     */
    public static <T> Response<T> failed() {
        return failed(ResultCode.FAILED);
    }




}
