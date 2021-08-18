package com.itran.fgoc.server.netty.handler;

import com.itran.fgoc.server.netty.MessageHeader;
import com.itran.fgoc.server.netty.var.MessageVar;
import io.netty.channel.ChannelHandlerContext;
import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * 定位数据批量上传
 *
 * 例子: 7E
 * 07 04
 * 00 62
 * 04 19 05 76 86 79
 * 00 17
 * 00 01 01 00 5D 00 00 00 00 00 0C 00 00 01 57 EF 82 06 CB 5C 28 00 29 00 00 00 00 21 08 18 15 02 19 01 04 00 00 00 0E 30 01 16 FE 36 E6 02 00 01 4C 07 00 21 4D 53 33 33 30 5F 56 32 2E 30 32 3B 4C 54 45 3A 32 32 3B 4D 32 3A 31 2C 31 30 3B 42 3A 34 2E 30 34 20 00 0A 89 86 04 93 14 21 70 21 50 92
 * 07
 * 7E
 *
 * @author chun
 * @date 2021/8/18 16:42
 */
@Data
@Component
public class LocationsUploadHandler implements BaseHandler {

    /**
     * 消息 id
     */
    private final String messageId = MessageVar.MessageId.LOCATIONS_UPLOAD;

    @Override
    public void handle(ChannelHandlerContext channelHandlerContext, byte[] decodeMessage, MessageHeader messageHeader, byte[] messageBody) {
        return;
    }
}
