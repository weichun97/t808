package com.itran.fgoc.server.netty.util;

import cn.hutool.core.codec.BCD;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.ObjectUtil;
import com.itran.fgoc.common.core.api.ResultCode;
import com.itran.fgoc.common.core.exception.ApiException;
import com.itran.fgoc.common.core.util.NumberUtil;
import com.itran.fgoc.server.netty.MessageHeader;
import com.itran.fgoc.server.netty.var.EncryptionVar;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;

import java.util.Arrays;

/**
 * The type T 808 utils.
 *
 * @author chun
 * @date 2021 /8/18 9:23
 */
@Slf4j
public class T808Utils {
    /**
     * 标识位
     */
    private final static byte[] FLAG = HexUtil.decodeHex("7E");

    /**
     * 转义还原消息
     * 0x7d 0x01 -> 0x7d
     * 0x7d 0x02 -> 0x7e
     *
     * @param bytes the bytes
     * @return byte [ ]
     */
    public static byte[] decodeMessage(byte[] bytes){
        String hexStr = HexUtil.encodeHexStr(bytes);
        return HexUtil.decodeHex(hexStr.replaceAll("7d01", "7d")
                .replaceAll("7d02", "7e"));
    }

    /**
     * 校验消息格式是否正确
     *
     * @param decodeMessage 转义后的消息
     * @return boolean
     */
    public static boolean checkMessage(byte[] decodeMessage){
        return checkFlag(decodeMessage) && checkCheckCode(decodeMessage);
    }

    /**
     * 获取消息头
     *
     * @param decodeMessage 转义后的消息
     * @return message header
     */
    public static MessageHeader getMessageHeader(byte[] decodeMessage){
        if(decodeMessage.length < 14){
            log.error("消息长度不能低于15位, 消息内容:{}", decodeMessage);
            throw new ApiException(ResultCode.FAILED);
        }
        byte[] header = Arrays.copyOfRange(decodeMessage, 1, 13);
        MessageHeader messageHeader = MessageHeader.builder()
                .messageId(HexUtil.encodeHexStr(Arrays.copyOfRange(header, 0, 2)))
                .clientNumber(String.valueOf(Long.parseLong(BCD.bcdToStr(Arrays.copyOfRange(header, 4, 10)))))
                .messageSerialNumber(NumberUtil.binaryToLong(NumberUtil.byteToBit(header[10]) + NumberUtil.byteToBit(header[11])))
                .build();

        parseMessageBodyAttibute(messageHeader, header);
        parseMessagePackage(messageHeader, decodeMessage);
        return messageHeader;
    }

    /**
     * 获取消息体
     *
     * @param messageHeader 消息头
     * @param decodeMessage 转义后的消息
     * @return the byte [ ]
     */
    public static byte[] getMessageBody(@NonNull MessageHeader messageHeader, byte[] decodeMessage){
        return Arrays.copyOfRange(decodeMessage, FLAG.length + messageHeader.getLength() , FLAG.length + messageHeader.getLength() + messageHeader.getMessageBodyLength());
    }

    /**
     * 校验标识位是否正确
     *
     * @param decodeMessage 转义后的字节
     * @return
     */
    private static boolean checkFlag(byte[] decodeMessage){
        byte[] startFlag = Arrays.copyOfRange(decodeMessage, 0, 1);
        byte[] endFlag = Arrays.copyOfRange(decodeMessage, decodeMessage.length - 1, decodeMessage.length);
        return Arrays.equals(startFlag, FLAG) && Arrays.equals(endFlag, FLAG);
    }

    /**
     * 校验检验码是否正确
     *
     * @param decodeMessage 转义后的字节
     * @return
     */
    private static boolean checkCheckCode(byte[] decodeMessage){
        byte start = decodeMessage[0];
        for (int i = 1; i < decodeMessage.length; i++) {
            start ^= decodeMessage[i];
        }
        return start == Byte.parseByte("0");
    }

    /**
     * 解析消息包封装项
     * @param messageHeader 消息头包装类
     * @param decodeMessage 转义后的消息
     */
    private static void parseMessagePackage(MessageHeader messageHeader, byte[] decodeMessage) {
        if(!messageHeader.isSubcontract()){
            return;
        }
        messageHeader.setCount(Long.valueOf(HexUtil.encodeHexStr(Arrays.copyOfRange(decodeMessage, 13, 15)), 16));
        messageHeader.setCurrentNumber(Long.valueOf(HexUtil.encodeHexStr(Arrays.copyOfRange(decodeMessage, 15, 17)), 16));
    }

    /**
     * 解析消息体属性
     *
     * @param messageHeader 消息头包装类
     * @param decodeMessage 转义后的消息
     */
    private static void parseMessageBodyAttibute(MessageHeader messageHeader, byte[] decodeMessage) {
        String binaryStr = NumberUtil.byteToBit(decodeMessage[2]) + NumberUtil.byteToBit(decodeMessage[3]);

        // 保留字
        messageHeader.setReserve(binaryStr.substring(0, 2));

        // 是否分包
        messageHeader.setSubcontract(ObjectUtil.equal(binaryStr.substring(2, 3), "1"));

        // 默认消息头长度11，有分包信息加4
        messageHeader.setLength(messageHeader.isSubcontract() ? 16 : 12);

        // 消息体长度
        messageHeader.setMessageBodyLength(NumberUtil.binaryToInt(binaryStr.substring(6)));
        if(ObjectUtil.equal(binaryStr.substring(5, 6), "1")){
            messageHeader.setEncryption(EncryptionVar.RSC);
        }else{
            messageHeader.setEncryption(EncryptionVar.NULL);
        }
    }
}
