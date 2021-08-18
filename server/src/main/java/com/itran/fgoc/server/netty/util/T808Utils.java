package com.itran.fgoc.server.netty.util;

import cn.hutool.core.codec.BCD;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.ObjectUtil;
import com.itran.fgoc.common.core.api.ResultCode;
import com.itran.fgoc.common.core.exception.ApiException;
import com.itran.fgoc.common.core.util.NumberUtil;
import com.itran.fgoc.common.core.util.StrUtil;
import com.itran.fgoc.server.netty.MessageHeader;
import com.itran.fgoc.server.netty.var.MessageVar;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The type T 808 utils.
 *
 * @author chun
 * @date 2021 /8/18 9:23
 */
@Slf4j
public class T808Utils {

    /**
     * 流水号容器
     */
    private static final Map<String, Long> MESSAGE_SERIAL_NUMBER_MAP = new ConcurrentHashMap<>();

    /**
     * 标识位
     */
    private final static byte[] FLAG = HexUtil.decodeHex("7E");

    /**
     * 获取消息的流水号
     *
     * @param messageId the message id
     * @return long
     */
    public synchronized static Long getMessageSerialNumber(String messageId){
        long messageSerialNumber = 1;
        if(MESSAGE_SERIAL_NUMBER_MAP.containsKey(messageId) && MESSAGE_SERIAL_NUMBER_MAP.get(messageId) != Long.MAX_VALUE){
            messageSerialNumber = MESSAGE_SERIAL_NUMBER_MAP.get(messageId) + 1;
        }
        MESSAGE_SERIAL_NUMBER_MAP.put(messageId, messageSerialNumber);
        return messageSerialNumber;
    }

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
     * 消息转义
     * 0x7d -> 0x7d 0x01
     * 0x7e -> 0x7d 0x02
     *
     * @param bytes the bytes
     * @return byte [ ]
     */
    public static byte[] encodeMessage(byte[] bytes){
        String hexStr = HexUtil.encodeHexStr(bytes);
        return HexUtil.decodeHex(hexStr.replaceAll("7d", "7d01")
                .replaceAll("7e", "7d02"));
    }

    /**
     * 校验消息格式是否正确
     *
     * @param decodeMessage 转义后的消息
     * @return boolean boolean
     */
    public static boolean checkMessage(byte[] decodeMessage){
        return checkFlag(decodeMessage) && checkCheckCode(decodeMessage);
    }

    /**
     * 创建消息
     *
     * @param messageId    消息 id
     * @param clientNumber 终端手机号
     * @param bodyMessage  消息体
     * @return the byte [ ]
     */
    public static byte[] createMessage(String messageId, String clientNumber, byte[] bodyMessage){
        return createMessage(MessageHeader.builder()
                .messageId(messageId)
                .reserve(MessageVar.Reserve.DEFAULT)
                .subcontract(false)
                .encryption(MessageVar.Encryption.DEFAULT)
                .messageBodyLength(bodyMessage.length)
                .clientNumber(clientNumber)
                .messageSerialNumber(getMessageSerialNumber(messageId))
                .build(), bodyMessage);
    }

    /**
     * 创建消息
     *
     * @param messageHeader 消息头对象
     * @param bodyMessage   消息体
     * @return the byte [ ]
     */
    public static byte[] createMessage(MessageHeader messageHeader, byte[] bodyMessage){
        StringBuilder hexStrBuild = new StringBuilder(messageHeader.getMessageId());

        // 消息体属性
        String messageBodyAttributeBinaryStr = messageHeader.getReserve() +
                (messageHeader.isSubcontract() ? "1" : "0") +
                messageHeader.getEncryption() +
                StrUtil.addZeroForNum(NumberUtil.getBinaryStr(bodyMessage.length), 9);
        String messageBodyAttributeHexStr = HexUtil.encodeHexStr(new byte[]{Byte.parseByte(messageBodyAttributeBinaryStr.substring(0, 8), 2), Byte.parseByte(messageBodyAttributeBinaryStr.substring(8), 2)});
        hexStrBuild.append(messageBodyAttributeHexStr);

        // 终端手机号
        String clientNumberHexStr = StrUtil.addZeroForNum(HexUtil.encodeHexStr(BCD.strToBcd(messageHeader.getClientNumber())), 12);
        hexStrBuild.append(clientNumberHexStr);

        // 流水号
        String messageSerialNumberHexStr = StrUtil.addZeroForNum(HexUtil.toHex(messageHeader.getMessageSerialNumber()), 4);
        hexStrBuild.append(messageSerialNumberHexStr);

        // 消息包封装项
        if(messageHeader.isSubcontract()){
            // TODO: 2021/8/18 目前不考虑分包，使用到再实现
        }

        byte[] messageHeadAndBody = ArrayUtil.addAll(HexUtil.decodeHex(hexStrBuild.toString()), bodyMessage);
        byte checkCode = getCheckCode(messageHeadAndBody);

        return ArrayUtil.addAll(FLAG, messageHeadAndBody, new byte[]{checkCode}, FLAG);
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

        // 消息头长度：默认消息头长度11，有分包信息加4
        messageHeader.setLength(messageHeader.isSubcontract() ? 16 : 12);

        messageHeader.setOriginal(Arrays.copyOfRange(decodeMessage, 1, 1 + messageHeader.getLength()));

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
     * 获取检验码
     *
     * @param bytes 要生成校验码的字节
     * @return
     */
    private static byte getCheckCode(byte[] bytes){
        byte checkCode = bytes[0];
        for (int i = 1; i < bytes.length; i++) {
            checkCode ^= bytes[i];
        }
        return checkCode;
    }

    /**
     * 校验检验码是否正确
     *
     * @param decodeMessage 转义后的字节
     * @return
     */
    private static boolean checkCheckCode(byte[] decodeMessage){
        byte checkCode = getCheckCode(Arrays.copyOfRange(decodeMessage, 0, decodeMessage.length - 1));
        return checkCode == decodeMessage[decodeMessage.length - 1];
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

        // 加密方式
        messageHeader.setEncryption(binaryStr.substring(3, 6));

        // 消息体长度
        messageHeader.setMessageBodyLength(NumberUtil.binaryToInt(binaryStr.substring(6)));
    }
}
