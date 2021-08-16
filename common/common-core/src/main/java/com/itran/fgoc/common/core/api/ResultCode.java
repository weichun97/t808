package com.itran.fgoc.common.core.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author:
 * @create: 2020-08-03 14:53
 * @description: 通用返回信息
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ResultCode implements IErrorCode {

    SUCCESS(200, "操作成功"),
    FAILED(500, "操作失败"),
    UNAUTHORIZED(401, "暂未登录或token已经过期"),
    VALIDATE_FAILED(402, "参数检验失败"),
    FORBIDDEN(403, "没有相关权限"),
    LOGIN_FAILED(405, "登录失败,帐号或密码错误"),
    LOGIN_CODE_TIMEOUT(405, "登录失败,验证码已过期"),
    LOGIN_CODE_FAILED(405, "登录失败,验证码输入错误"),
    DATA_IS_NULL(501, "操作的数据异常"),
    REMOTE_CALL_FAILED(502, "远程调用失败"),
    INSERT_ERROR(5000,"插入数据失败！"),
    UPDATE_ERROR(5001,"修改数据失败！"),
    DELETE_ERROR(5002,"删除数据失败！");


//    CODE_20101001(20101001, "航班计划详情获取失败"),
    ;
    private long code;
    private String message;

}
