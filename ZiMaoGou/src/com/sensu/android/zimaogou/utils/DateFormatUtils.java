package com.sensu.android.zimaogou.utils;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/*
 * @author Msquirrel
 */
@SuppressLint("SimpleDateFormat")
public class DateFormatUtils {

	private static SimpleDateFormat sf = null;

	/**
	 * 获取系统时间 格式为："yyyy/MM/dd "
	 * **/
	public static String getFormatCurDate() {
		Date d = new Date();
		sf = new SimpleDateFormat("yyyy/MM/dd");
		return sf.format(d);
	}

	/**
	 * 获取系统时间 格式为："yyyy-MM-dd HH:mm:ss"
	 * **/
	public static String getFormatCurDate2() {
		Date d = new Date();
		sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sf.format(d);
	}

	/**
	 * 获取系统时间 格式为："yyyy-MM-dd HH:mm"
	 * **/
	public static String getFormatCurDate3() {
		Date d = new Date();
		sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return sf.format(d);
	}

	/* 时间戳转换成yyyy年MM月dd日 */
	public static String getDateToString(long time) {
		Date d = new Date(time);
		sf = new SimpleDateFormat("yyyy年MM月dd日");
		return sf.format(d);
	}

	/**
	 * 时间戳转换成yyyy-MM-dd timeStr:为秒单位
	 * **/
	public static String getDateToSimpleString(String timeStr) {
		if (TextUtils.isEmpty(timeStr))
			return "";

		long time = Long.parseLong(timeStr);
		Date d = new Date(time * 1000);
		sf = new SimpleDateFormat("yyyy-MM-dd");
		return sf.format(d);
	}

	/* 将字符串转为时间戳 yyyy-MM-dd */
	public static long getStringToDate(String time) {
		long result = 0;
		if (time.indexOf("-") > 0) {
			sf = new SimpleDateFormat("yyyy-MM-dd");
		} else if (time.indexOf("/") > 0) {
			sf = new SimpleDateFormat("yyyy/MM/dd");
		} else {
			return result;
		}
		Date date = null;
		try {
			date = sf.parse(time);
			result = date.getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date.getTime();
	}

	/* 将字符串转为时间戳 yyyy-MM-dd hh:ss:mm */
	public static long getMillsByStringDateTime(String time) {
		long result = 0;
		if (time.indexOf("-") > 0) {
			sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		} else if (time.indexOf("/") > 0) {
			sf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		} else {
			return result;
		}
		Date date = null;
		try {
			date = sf.parse(time);
			result = date.getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * result: yyyy-MM-dd HH:mm:ss
	 * **/
	public static String converDateTimeToString(Date date) {
		sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sf.format(date);
	}

	private static int[] splitTimeZone() {
		int[] result = { 0, 0 };

		Locale defaulLocal = Locale.getDefault();
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"),
				defaulLocal);
		Date currentLocalTime = calendar.getTime();
		DateFormat date = new SimpleDateFormat("Z", defaulLocal);
		String localTime = date.format(currentLocalTime);
		if (!TextUtils.isEmpty(localTime)) {
			localTime = localTime.toLowerCase(defaulLocal);
			int start_add = localTime.indexOf("+");
			int start_subtract = localTime.indexOf("-");
			if (start_add >= 0) {
				String _short_time = localTime.substring(start_add + 1);
				if (_short_time.length() >= 2) {
					result[0] = Integer.parseInt(_short_time.substring(0, 2));
				}
				if (_short_time.length() >= 4) {
					int startMH = _short_time.indexOf(":");
					if (startMH >= 0) {
						result[1] = Integer.parseInt(_short_time.substring(
								startMH + 1, 5));
					} else {
						result[1] = Integer.parseInt(_short_time
								.substring(2, 4));
					}
				}
			} else if (start_subtract >= 0) {
				String _short_time = localTime.substring(start_subtract + 1);
				if (_short_time.length() >= 2) {
					result[0] = Integer.parseInt(_short_time.substring(0, 2))
							* -1;
				}
				if (_short_time.length() >= 4) {
					result[1] = Integer.parseInt(_short_time.substring(2, 4));
				}
			}
		}
		return result;
	}

	public static long getTimeZoneMillis() {
		int[] timeZoneArray = splitTimeZone();
		int timeZone = 0;
		if (timeZoneArray[0] > 0) {
			timeZone = timeZoneArray[0] * 60 + timeZoneArray[1];
		} else {
			timeZone = timeZoneArray[0] * 60 - timeZoneArray[1];
		}
		return timeZone * 60 * 1000;
	}
}