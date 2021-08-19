package com.itran.fgoc.server.netty.util;

import cn.hutool.core.collection.ConcurrentHashSet;
import cn.hutool.core.util.HexUtil;
import com.itran.fgoc.server.netty.MessageHeader;
import com.itran.fgoc.server.netty.var.MessageVar;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
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
    public void createMessageTest1(){
        byte[] message = T808Utils.createMessage(MessageVar.MessageId.SERVER_COMMON_RESPONSE, "41905768679", HexUtil.decodeHex("0002010200"));

        Assert.assertEquals("7e8001000504190576867900010002010200157e", HexUtil.encodeHexStr(message));
    }

    @Test
    public void createMessageTest2() {
        byte[] message = T808Utils.createMessage(MessageHeader.builder()
                .messageId(MessageVar.MessageId.SERVER_COMMON_RESPONSE)
                .messageBodyLength(5)
                .encryption(MessageVar.Encryption.DEFAULT)
                .subcontract(false)
                .reserve(MessageVar.Reserve.DEFAULT)
                .clientNumber("41905768679")
                .messageSerialNumber(1L)
                .build(), HexUtil.decodeHex("0002010200"));

        Assert.assertEquals("7e8001000504190576867900010002010200157e", HexUtil.encodeHexStr(message));
    }

    /**
     * 测试多线程下生成流水号是否正常
     * @throws InterruptedException
     */
    @Test
    public void getMessageSerialNumberTest() throws InterruptedException {
        CountDownLatch countDownLatch  = new CountDownLatch(100);
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        Set<Long> messageSerialNumbers = new ConcurrentHashSet<>();
        for (int i = 1; i <= 100; i++) {
            executorService.execute(() -> {
                try {
                    Long messageSerialNumber = T808Utils.getMessageSerialNumber(MessageVar.MessageId.SERVER_COMMON_RESPONSE);
                    messageSerialNumbers.add(messageSerialNumber);
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        // 5个线程countDown()都执行之后才会释放当前线程,程序才能继续往后执行
        countDownLatch.await();
        //关闭线程池
        executorService.shutdown();

        Assert.assertEquals(100, messageSerialNumbers.size());
    }
}
