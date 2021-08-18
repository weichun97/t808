package com.itran.fgoc.server.netty;

import com.itran.fgoc.server.netty.var.EncryptionVar;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author chun
 * @date 2021/8/17 14:32
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageHeader implements Serializable {

    private static final long serialVersionUID = -1916768423756534435L;

    /**
     * 消息 id
     */
    private String messageId;

    /**
     * 消息体长度
     */
    private Integer messageBodyLength;

    /**
     * 加密方式
     * @see EncryptionVar
     */
    private Integer encryption;

    /**
     * 是否分包
     */
    private boolean subcontract;

    /**
     * 消息体保留字
     */
    private String reserve;

    /**
     * 终端手机号
     */
    private String clientNumber;

    /**
     * 消息流水号
     */
    private Long messageSerialNumber;

    /**
     * 分包总数
     */
    private Long count;

    /**
     * 包序号
     */
    private Long currentNumber;

    /**
     * 消息头长度
     */
    private Integer length;
}
