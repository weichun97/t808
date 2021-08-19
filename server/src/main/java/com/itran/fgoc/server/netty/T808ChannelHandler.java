package com.itran.fgoc.server.netty;

import com.itran.fgoc.server.netty.handler.HandlerFactory;
import com.itran.fgoc.server.netty.util.T808Utils;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.codec.string.StringEncoder;

import java.nio.CharBuffer;
import java.nio.charset.Charset;

public class T808ChannelHandler extends SimpleChannelInboundHandler<byte[]> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, byte[] decodeMessage) throws Exception {
        MessageHeader messageHeader = T808Utils.getMessageHeader(decodeMessage);
        byte[] messageBody = T808Utils.getMessageBody(messageHeader, decodeMessage);
        HandlerFactory.get(messageHeader.getMessageId()).handle(channelHandlerContext, decodeMessage, messageHeader, messageBody);
    }
}
