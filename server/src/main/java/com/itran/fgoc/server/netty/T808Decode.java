package com.itran.fgoc.server.netty;

import com.itran.fgoc.server.netty.util.T808Utils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class T808Decode extends MessageToMessageDecoder<ByteBuf> {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> out) throws Exception {
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);

        // 消息转义+校验
        byte[] decodeMessage = T808Utils.decodeMessage(bytes);
        if(!T808Utils.checkMessage(decodeMessage)){
            log.error("消息校验失败：原始消息:{}", bytes);
            return;
        }
        out.add(decodeMessage);
    }
}
