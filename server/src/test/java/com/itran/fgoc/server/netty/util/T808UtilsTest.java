package com.itran.fgoc.server.netty.util;

import cn.hutool.core.util.HexUtil;
import com.itran.fgoc.server.netty.MessageHeader;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class T808UtilsTest {

    @Test
    public void decodeMessageTest() {
        String hexStr = "7E7D017D02000C0419057686790000303134333035333830353434937E";
        byte[] decodeMessage = T808Utils.decodeMessage(HexUtil.decodeHex(hexStr));
        Assert.assertEquals(HexUtil.encodeHexStr(decodeMessage), "7e7d7e000c0419057686790000303134333035333830353434937e");
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
}
