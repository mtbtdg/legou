package cn.zxJava.controller;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {

	/**
	 * 使用md5的算法进行加密
	 */
	public static String md5(String plainText) {
		byte[] secretBytes = null;
		try {
			secretBytes = MessageDigest.getInstance("md5").digest(plainText.getBytes());
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("没有md5这个算法！");
		}

		String md5code = new BigInteger(1, secretBytes).toString(16);// 16进制数字
		// 如果生成数字未满32位，需要前面补0
		for (int i = 0; i < 32 - md5code.length(); i++) {
			md5code = "0" + md5code;
		}
		return md5code;
	}

	// 202cb962ac59075b964b07152d234b70
	public static void main(String[] args) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		/*
		// 加密
		for (int i = 0; i < 5; i++) {
		    // 加密
			String value = encoder.encode("123");
            System.out.println(value);
		}

		$2a$10$nShrS57QMt.yU8/5q2xSVu5nHYKj8dKLPXnKDXFicEU1mRFENUihO
        $2a$10$3UZo3UuS//nhDXuTOObINuwwlsSra9TQnajETp0O2mH3cX6.JXyO2
        $2a$10$6NfZQ81ZQYvo534yg49Ca.gbs0vFb7u8ikq3cnQgd0iyUSYo2B0m.
        $2a$10$tclXU3SlSntsmWF.0zPEweDkMjtTN/itnk/dl9eJvHeYnbKMH8Ulu
        $2a$10$qr6pqB6oubuyySWo7pnXMuVBQTHnbvHOQUtobnby7lmZyMkhlgPQO
		*/

		// 使用提供的方法
        boolean b = encoder.matches("1234", "$2a$10$qr6pqB6oubuyySWo7pnXMuVBQTHnbvHOQUtobnby7lmZyMkhlgPQO");
        System.out.println(b);

    }

}
