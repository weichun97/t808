package com.itran.fgoc.common.core.exception;

import com.itran.fgoc.common.core.api.IErrorCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * The type Api exception.
 *
 * @author chun
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiException extends RuntimeException {
    /**
     * 异常码
     */
    private IErrorCode errorCode;

    /**
     * 自定义错误描述
     */
    private String msg;

    /**
     * Instantiates a new Api exception.
     *
     * @param iErrorCode the error code
     */
    public ApiException(IErrorCode iErrorCode){
        this.errorCode = iErrorCode;
    }
}
