package com.itran.fgoc.server.netty.handler;

import com.itran.fgoc.server.netty.MessageHeader;
import com.itran.fgoc.server.netty.var.MessageVar;
import io.netty.channel.ChannelHandlerContext;
import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * 客户端注册
 *
 * 例子: 7E
 * 01 00
 * 00 36
 * 04 19 05 76 86 79
 * 00 00
 * 00 2C 01 33 37 30 32 31 37 4D 53 33 33 30 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 35 37 36 38 36 37 39 00 30 30 38 36 32 34 31 39 30 35 37 36 38 36 37 39 31
 * A4
 * 7E
 *
 * @author chun
 * @date 2021/8/18 11:19
 */
@Data
@Component
public class RegisterHandler implements BaseHandler {

    /**
     * 消息 id
     */
    private final String messageId = MessageVar.MessageId.REGISTER;

    @Override
    public void handle(ChannelHandlerContext channelHandlerContext, byte[] decodeMessage, MessageHeader messageHeader, byte[] messageBody) {
        channelHandlerContext.channel().writeAndFlush(null);
    }
}
