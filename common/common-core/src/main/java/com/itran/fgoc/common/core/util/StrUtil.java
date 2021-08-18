package com.itran.fgoc.common.core.util;

/**
 * @author chun
 * @date 2021/8/18 14:44
 */
public class StrUtil extends cn.hutool.core.util.StrUtil {

    /**
     * 指定长度，给定字符串长度不足时前面补 0
     * @param str 字符串
     * @param strLength 指定长度
     * @return 补0后的字符串
     */
    public static String addZeroForNum(String str, int strLength) {
        int strLen = str.length();
        if (strLen < strLength) {
            while (strLen < strLength) {
                StringBuffer sb = new StringBuffer();
                sb.append("0").append(str);// 左补0
                str = sb.toString();
                strLen = str.length();
            }
        }
        return str;
    }
}
