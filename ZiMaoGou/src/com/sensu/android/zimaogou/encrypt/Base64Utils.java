package com.sensu.android.zimaogou.encrypt;

import android.util.Base64;

import java.lang.reflect.Method;

/**
 * Created by winter on 2015/9/22.
 * 字符编码
 * @author winter
 */
public class Base64Utils {

	public static String encodeBase64(byte[] input) throws Exception {
		Class clazz = Class.forName("com.sun.org.apache.xerces.internal.impl.dv.util.Base64");
		Method mainMethod = clazz.getMethod("encode", byte[].class);
		mainMethod.setAccessible(true);
		Object retObj = mainMethod.invoke(null, new Object[]{input});
		return (String) retObj;
	}

	public static byte[] decodeBase64(String input) throws Exception {
		Class clazz = Class.forName("com.sun.org.apache.xerces.internal.impl.dv.util.Base64");
		Method mainMethod = clazz.getMethod("decode", String.class);
		mainMethod.setAccessible(true);
		Object retObj = mainMethod.invoke(null, input);
		return (byte[]) retObj;
	}

	public static byte[] encode(byte[] input, int flags) {
		return Base64.encode(input, flags);
	}

	public static byte[] encode(byte[] input, int offset, int len, int flags) {
		return Base64.encode(input, offset, len, flags);
	}

	public static String encodeToString(byte[] input, int flags) {
		return Base64.encodeToString(input, flags);
	}

	public static String encodeToString(byte[] input, int offset, int len, int flags) {
		return Base64.encodeToString(input, offset, len, flags);
	}

	public static byte[] decode(byte[] input, int flags) {
		return Base64.decode(input, flags);
	}

	public static byte[] decode(byte[] input, int offset, int len, int flags) {
		return Base64.decode(input, offset, len, flags);
	}

	public static byte[] decode(String str, int flags) {
		return Base64.decode(str, flags);
	}
}
