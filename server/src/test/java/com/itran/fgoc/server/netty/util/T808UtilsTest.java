package com.itran.fgoc.server.netty.util;

import cn.hutool.core.util.HexUtil;
import com.itran.fgoc.common.core.util.NumberUtil;
import com.itran.fgoc.common.core.util.StrUtil;
import com.itran.fgoc.server.netty.MessageHeader;
import com.itran.fgoc.server.netty.var.MessageVar;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class T808UtilsTest {

    @Test
    public void decodeMessageTest() {
        String hexStr = "7E7D017D02000C0419057686790000303134333035333830353434937E";
        byte[] decodeMessage = T808Utils.decodeMessage(HexUtil.decodeHex(hexStr));
        Assert.assertEquals(HexUtil.encodeHexStr(decodeMessage), "7e7d7e000c0419057686790000303134333035333830353434937e");
    }

    @Test
    public void encodeMessageTest() {
        String hexStr = "7d7e000c041905768679000030313433303533383035343493";
        byte[] encodeMessage = T808Utils.encodeMessage(HexUtil.decodeHex(hexStr));
        Assert.assertEquals(HexUtil.encodeHexStr(encodeMessage), "7d017d02000c041905768679000030313433303533383035343493");
    }

    @Test
    public void checkMessageTest(){
        String hexStr = "7E0102000C0419057686790000303134333035333830353434937E";
        byte[] bytes = HexUtil.decodeHex(hexStr);
        Assert.assertTrue(T808Utils.checkMessage(bytes));
    }

    @Test
    public void getMessageHeaderTest(){
        String hexStr = "7E01000036041905768679001D002C013337303231374D5333333000000000000000000000000000000035373638363739003030383632343139303537363836373931B97E";
        MessageHeader messageHeader = T808Utils.getMessageHeader(T808Utils.decodeMessage(HexUtil.decodeHex(hexStr)));
        Assert.assertEquals(messageHeader.getMessageId(), "0100");
        Assert.assertEquals(messageHeader.getMessageBodyLength(), Integer.valueOf(54));
        Assert.assertEquals(messageHeader.getLength(), Integer.valueOf(11));
    }

    @Test
    public void getMessageBodyTest() {
        String hexStr = "7E01000036041905768679001D002C013337303231374D5333333000000000000000000000000000000035373638363739003030383632343139303537363836373931B97E";
        byte[] decodeMessage = T808Utils.decodeMessage(HexUtil.decodeHex(hexStr));
        MessageHeader messageHeader = T808Utils.getMessageHeader(decodeMessage);
        byte[] body = T808Utils.getMessageBody(messageHeader, decodeMessage);
        Assert.assertEquals("002c013337303231374d5333333000000000000000000000000000000035373638363739003030383632343139303537363836373931", HexUtil.encodeHexStr(body));
    }

    @Test
    public void createMessageTest() {
        byte[] message = T808Utils.createMessage(MessageHeader.builder()
                .messageId(MessageVar.MessageId.SERVER_COMMON_ANSWER)
                .messageBodyLength(5)
                .encryption(MessageVar.Encryption.NULL)
                .subcontract(false)
                .reserve(MessageVar.Reserve.NULL)
                .clientNumber("41905768679")
                .messageSerialNumber(1L)
                .build(), HexUtil.decodeHex("0002010200"));
        Assert.assertEquals("7e8001000504190576867900010002010200157e", HexUtil.encodeHexStr(message));
    }

    @Test
    public void getMessageSerialNumberTest() {
        // 1. 定义闭锁来拦截线程
        final CountDownLatch startGate = new CountDownLatch(1);
        final CountDownLatch endGate  = new CountDownLatch(100);

        // 2. 创建指定数量的线程
        for (int i = 0; i <100; i++) {
            Thread t = new Thread(() -> {
                try {
                    startGate.await();
                    try {
                        ((Runnable) () -> {
                            Integer messageSerialNumber = T808Utils.getMessageSerialNumber(MessageVar.MessageId.SERVER_COMMON_ANSWER);
                            System.out.println(messageSerialNumber);
                        }).run();
                    } finally {
                        endGate.countDown();
                    }
                } catch (InterruptedException e) {

                }
            });

            t.start();
        }

        // 3. 线程统一放行，并记录时间！
        long start =  System.nanoTime();

        startGate.countDown();
        try {
            endGate.await();
        } catch (InterruptedException e) {
        }

        long end = System.nanoTime();
        System.out.println("cost times :" +(end - start));

//        Integer messageSerialNumber1 = T808Utils.getMessageSerialNumber(MessageVar.MessageId.SERVER_COMMON_ANSWER);
//        Assert.assertEquals(Integer.valueOf(1), messageSerialNumber1);
//        Integer messageSerialNumber2 = T808Utils.getMessageSerialNumber(MessageVar.MessageId.SERVER_COMMON_ANSWER);
//        Assert.assertEquals(Integer.valueOf(2), messageSerialNumber2);
    }
}
