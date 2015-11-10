package com.sensu.android.zimaogou.encrypt;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

/**
 * Created by winter on 2015/9/22.
 * 加密后的byte数组是不能强制转换成字符串的，换言之：字符串和byte数组在这种情况下不是互逆的；
 * 要避免这种情况，我们需要做一些修订，可以考虑将二进制数据转换成十六进制表示
 * 可以试试Base64编码
 * @author winter
 */
public class AESUtils {

	public static byte[] encrypt(String content, String password) throws Exception {
		return encrypt(content.getBytes("utf-8"), password);
	}

	public static byte[] encrypt(byte [] byteContent, String password) throws Exception {
		//生成指定算法（AES）的密钥生成器对象
		KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
		//初始化此密钥生成器，使其具有确定的密钥大小(AES 要求密钥长度为 128)
		keyGenerator.init(128, new SecureRandom(password.getBytes()));
		//生成一个密钥
		SecretKey secretKey = keyGenerator.generateKey();
		byte[] enCodeFormat = secretKey.getEncoded();

		return encrypt(byteContent, enCodeFormat);
	}

	/**
	 * 加密方式有两种限制
	 * 1.密钥必须是16位的
	 * 2.待加密内容的长度必须是16的倍数，如果不是16的倍数，就会出如下异常:
	 * javax.crypto.IllegalBlockSizeException: Input length not multiple of 16 bytes
	 * Cipher.getInstance("AES/ECB/NoPadding")
	 */
	public static byte[] encrypt(byte[] byteContent, byte[] enCodeFormat) throws Exception {
		//转换成密钥对象
		SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
		Cipher cipher = Cipher.getInstance("AES");// 创建密码器
		cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
		return cipher.doFinal(byteContent); // 加密
	}

	public static byte[] decrypt(byte[] content, String password) throws Exception {
		KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
		keyGenerator.init(128, new SecureRandom(password.getBytes()));
		SecretKey secretKey = keyGenerator.generateKey();
		byte[] enCodeFormat = secretKey.getEncoded();

		return decrypt(content, enCodeFormat);
	}

	/**
	 * 加密方式有两种限制
	 * 1.密钥必须是16位的
	 * 2.待加密内容的长度必须是16的倍数，如果不是16的倍数，就会出如下异常:
	 * javax.crypto.IllegalBlockSizeException: Input length not multiple of 16 bytes
	 * Cipher.getInstance("AES/ECB/NoPadding")
	 */
	public static byte[] decrypt(byte[] content, byte[] enCodeFormat) throws Exception {
		SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
		Cipher cipher = Cipher.getInstance("AES");// 创建密码器
		cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
		return cipher.doFinal(content); // 加密
	}

	/**
	 * 将二进制转换成16进制
	 * 可以试试Base64编码
	 *
	 * @param buf
	 * @return
	 */
	public static String parseByte2HexStr(byte buf[]) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase());
		}
		return sb.toString();
	}

	/**
	 * 将16进制转换为二进制
	 * 可以试试Base64编码
	 *
	 * @param hexStr
	 * @return
	 */
	public static byte[] parseHexStr2Byte(String hexStr) {
		if (hexStr.length() < 1)
			return null;
		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
			result[i] = (byte) (high * 16 + low);
		}
		return result;
	}
}
