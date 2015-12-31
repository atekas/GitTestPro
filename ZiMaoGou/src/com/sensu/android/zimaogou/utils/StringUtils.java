package com.sensu.android.zimaogou.utils;

import java.text.DecimalFormat;

/**
 * Created by zhangwentao on 2015/12/31.
 */
public class StringUtils {

    //字符串去掉最后一位的0
    public static String deleteZero(String string) {
        String newString = string;
        if (string.endsWith(".00")) {
            newString = string.substring(0, string.length() - 3);
        } else if (string.endsWith("0")) {
            newString = string.substring(0, string.length() - 1);
        }
        return newString;
    }

    //double保留两位小数  返回带两位小数的字符串
    public static String getDoubleWithTwo(double db) {
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        return decimalFormat.format(db);
    }
}
