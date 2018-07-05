package com.lcsd.examines.fengtai.util;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

/**
 * 字符串工具集
 *
 * @autor:Bin
 * @version:1.0
 * @created:2015-4-29 上午8:36:38
 **/
public class StringUtils {
    /**
     * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     *
     * @param input
     * @return boolean
     */
    public static boolean isEmpty(String input) {
        if (input == null || "".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }



    //unix格式时间转换
    public static String timeStamp2Date(String timestampString) {
        Long timestamp = Long.parseLong(timestampString) * 1000;
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(timestamp));
        return date;//unix时间戳转换成java时间,不然会只显示1970.。。。。
    }

    public static String join(Collection<? extends Object> collection, String separator) {
        StringBuilder sb = new StringBuilder();
        int i = 0;

        for (Object object : collection) {
            sb.append(object.toString());
            if (i < collection.size() - 1) {
                sb.append(separator);
            }
            i++;
        }
        return sb.toString();
    }

    public static String NowTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm ");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        return str;
    }

}
