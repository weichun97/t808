package com.itran.fgoc.server.netty;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

public class T808Decode extends MessageToMessageDecoder<ByteBuf> {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> out) throws Exception {
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        String encodeHexStr = HexUtil.encodeHexStr(bytes);
        String[] split = StrUtil.split(encodeHexStr, 2);
    }
}
