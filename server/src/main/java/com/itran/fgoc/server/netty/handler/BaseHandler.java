package com.itran.fgoc.server.netty.handler;

import com.itran.fgoc.server.netty.MessageHeader;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author chun
 * @date 2021/8/18 11:00
 */
public interface BaseHandler extends InitializingBean {

    /**
     * 消息 id
     * @return
     */
    String getMessageId();

    /**
     * 处理
     */
    void handle(ChannelHandlerContext channelHandlerContext, byte[] decodeMessage, MessageHeader messageHeader, byte[] messageBody);

    @Override
    default void afterPropertiesSet() throws Exception{
        HandlerFactory.register(getMessageId(), this);
    }
}
