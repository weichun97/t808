package com.itran.fgoc.server.netty;

import com.itran.fgoc.server.netty.handler.HandlerFactory;
import com.itran.fgoc.server.netty.util.T808Utils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class T808ChannelHandler extends SimpleChannelInboundHandler<byte[]> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, byte[] decodeMessage) throws Exception {
        MessageHeader messageHeader = T808Utils.getMessageHeader(decodeMessage);
        byte[] messageBody = T808Utils.getMessageBody(messageHeader, decodeMessage);
        HandlerFactory.get(messageHeader.getMessageId()).handle(messageBody);
    }
}
