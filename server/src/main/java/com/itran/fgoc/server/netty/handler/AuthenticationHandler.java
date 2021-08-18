package com.itran.fgoc.server.netty.handler;

import com.itran.fgoc.server.netty.MessageHeader;
import com.itran.fgoc.server.netty.var.MessageVar;
import io.netty.channel.ChannelHandlerContext;
import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * 客户端鉴权
 *
 * 例子: 7E
 * 01 02
 * 00 00
 * 04 19 05 76 86 79
 * 00 01
 * 93
 * 7E
 *
 * @author chun
 * @date 2021/8/18 16:25
 */
@Data
@Component
public class AuthenticationHandler implements BaseHandler {

    /**
     * 消息 id
     */
    private final String messageId = MessageVar.MessageId.AUTHENTICA;

    @Override
    public void handle(ChannelHandlerContext channelHandlerContext, byte[] decodeMessage, MessageHeader messageHeader, byte[] messageBody) {
        return;
    }
}
