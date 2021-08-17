package com.itran.fgoc.server;

import cn.hutool.core.codec.BCD;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.ObjectUtil;
import com.itran.fgoc.common.core.api.ResultCode;
import com.itran.fgoc.common.core.exception.ApiException;
import com.itran.fgoc.common.core.util.NumberUtil;
import com.itran.fgoc.server.netty.MessageHeader;
import com.itran.fgoc.server.netty.var.EncryptionVar;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * @author chun
 * @date 2021/8/17 9:51
 */
@Slf4j
public class ByteTests {

    /**
     * 标识位
     */
    public final byte[] FLAG = HexUtil.decodeHex("7E");

    @Test
    public void checkMessageTest(){
        String hexStr = "7E0102000C0419057686790000303134333035333830353434937E";
        byte[] bytes = HexUtil.decodeHex(hexStr);
        Assert.assertTrue(checkMessage(bytes));
    }

    @Test
    public void decodeMessageTest(){
        String hexStr = "7E7D017D0202000C0419057686790000303134333035333830353434937E";
        String s = HexUtil.encodeHexStr(decodeMessage(HexUtil.decodeHex(hexStr)));
        Assert.assertEquals("7e7d7e02000c0419057686790000303134333035333830353434937e", s);
    }

    @Test
    public void getMessageHeaderTest(){
        String hexStr = "7E01000036041905768679001D002C013337303231374D5333333000000000000000000000000000000035373638363739003030383632343139303537363836373931B97E";
        MessageHeader messageHeader = getMessageHeader(HexUtil.decodeHex(hexStr));
    }

    /**
     * 校验消息格式是否正确
     * @param bytes 原始报文
     * @return
     */
    private boolean checkMessage(byte[] bytes){
        // 消息转义
        byte[] decodeMessage = decodeMessage(bytes);

        // 校验标识位和检验码
        return checkFlag(decodeMessage) && checkCheckCode(decodeMessage);
    }

    /**
     * 校验标识位是否正确
     *
     * @param decodeMessage 转义后的字节
     * @return
     */
    private boolean checkFlag(byte[] decodeMessage){
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
    private boolean checkCheckCode(byte[] decodeMessage){
        byte start = decodeMessage[0];
        for (int i = 1; i < decodeMessage.length; i++) {
            start ^= decodeMessage[i];
        }
        return start == Byte.parseByte("0");
    }

    /**
     * 转义还原消息
     * 0x7d 0x01 -> 0x7d
     * 0x7d 0x02 -> 0x7e
     *
     * @param bytes
     * @return
     */
    private byte[] decodeMessage(byte[] bytes){
        String hexStr = HexUtil.encodeHexStr(bytes);
        return HexUtil.decodeHex(hexStr.replaceAll("7d01", "7d")
                .replaceAll("7d02", "7e"));
    }

    /**
     * 获取消息头
     * @param bytes 原始消息
     * @return
     */
    public MessageHeader getMessageHeader(byte[] bytes){
        if(bytes.length < 14){
            log.error("消息长度不能低于15位, 消息内容:{}", bytes);
            throw new ApiException(ResultCode.FAILED);
        }
        byte[] decodeMessage = decodeMessage(bytes);
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
     * 解析消息包封装项
     * @param messageHeader 消息头包装类
     * @param decodeMessage 转义后的消息
     */
    private void parseMessagePackage(MessageHeader messageHeader, byte[] decodeMessage) {
        Long aLong = Long.valueOf(HexUtil.encodeHexStr(Arrays.copyOfRange(decodeMessage, 13, 15)), 16);
        if(!messageHeader.isSubcontract()){
            return;
        }
    }

    /**
     * 解析消息体属性
     *
     * @param messageHeader 消息头包装类
     * @param decodeMessage 转义后的消息
     */
    private void parseMessageBodyAttibute(MessageHeader messageHeader, byte[] decodeMessage) {
        String binaryStr = NumberUtil.byteToBit(decodeMessage[2]) + NumberUtil.byteToBit(decodeMessage[3]);

        // 保留字
        messageHeader.setReserve(binaryStr.substring(0, 2));

        // 是否分包
        messageHeader.setSubcontract(ObjectUtil.equal(binaryStr.substring(2, 3), "1"));

        // 消息体长度
        messageHeader.setMessageBodyLength(NumberUtil.binaryToInt(binaryStr.substring(6)));
        if(ObjectUtil.equal(binaryStr.substring(5, 6), "1")){
            messageHeader.setEncryption(EncryptionVar.RSC);
        }else{
            messageHeader.setEncryption(EncryptionVar.NULL);
        }
    }


    @Test
    public void test(){

    }

}
