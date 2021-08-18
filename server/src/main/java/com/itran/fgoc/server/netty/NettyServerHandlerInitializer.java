package com.itran.fgoc.server.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;

public class NettyServerHandlerInitializer extends ChannelInitializer<Channel> {

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ch.pipeline()
            .addLast("t808Decoder", new T808Decode())
            .addLast(new T808ChannelHandler())
              ;
    }
}
