package com.itran.fgoc.server.netty.handler;

import java.util.HashMap;
import java.util.Map;

/**
 * The type Message factory.
 *
 * @author chun
 * @date 2021 /8/18 11:00
 */
public class HandlerFactory {

    /**
     * 消息类型容器
     */
    private static final Map<String, BaseHandler> CONTEXT = new HashMap<>();

    /**
     * 空消息
     */
    private static final NullHandler NULL_MESSAGE = new NullHandler();

    /**
     * 注册
     *
     * @param messageId the message id
     * @param message   the message
     */
    public static void register(String messageId, BaseHandler message){
        CONTEXT.put(messageId, message);
    }

    /**
     * 根据消息 id 获取消息
     *
     * @param messageId 消息 id
     * @return message
     */
    public static BaseHandler get(String messageId){
        return CONTEXT.getOrDefault(messageId, NULL_MESSAGE);
    }
}
