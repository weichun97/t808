package com.itran.fgoc.server.netty.handler;

import com.itran.fgoc.server.netty.var.MessageVar;
import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @author chun
 * @date 2021/8/18 11:19
 */
@Data
@Component
public class RegisterHandler implements BaseHandler {

    /**
     * 消息 id
     */
    private final String messageId = MessageVar.REGISTER;

    @Override
    public void handle(byte[] messageBody) {

    }
}
