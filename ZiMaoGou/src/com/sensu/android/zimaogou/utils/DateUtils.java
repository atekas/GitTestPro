package com.sensu.android.zimaogou.utils;

import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by qi.yang on 2015/12/15.
 */
public class DateUtils {

    /**
     *
     *
     *            the time to be formated
     * @return like X seconds/minutes/hours/weeks ago ,
     */
    public static String getTimeAgo(String dateTime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        long createTime = 0;
        try {
            createTime = format.parse(dateTime).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long curTime = System.currentTimeMillis();
        long between = (curTime - createTime) / 1000;
        Date date = new Date(createTime);
        String str = format.format(date);

        long day = between / (24 * 3600);
        long hour = between % (24 * 3600) / 3600;
        long minute = between % 3600 / 60;
        if (day >= 1 && day < 30) {
                str = day + "天前";
        } else if (hour > 0) {
            str = hour + "小时前";
        } else if (minute > 0) {
            str = ((minute == 0) ? 1 : minute) + "分钟前";
        } else {
            str = between + "刚刚";
        }

        return str;
    }
}
