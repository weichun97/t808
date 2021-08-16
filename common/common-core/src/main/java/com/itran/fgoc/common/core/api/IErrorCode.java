package com.itran.fgoc.common.core.api;


/**
 * @description: 封装API的错误码
 * @author:
 * @create: 2020-08-03 14:53
 */
public interface IErrorCode {
    /**
     * 异常编码
     * @return
     */
    long getCode();

    /**
     * 异常信息
     * @return
     */
    String getMessage();
}
