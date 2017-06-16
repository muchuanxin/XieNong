package com.xidian.xienong.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 验证密码是否合法
 * 
 * @author Bryan
 * @version 0.1
 * @crested 2013-8-2
 */
public class ComfirmPassword {
	private static String regex = "^[\\w_]{6,}+$";
	private static String numRegex = "[0-9]";
	private static String chRegex = "[a-zA-Z]";

	public static boolean isLegal(String password) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(password);
		if (!matcher.matches()) {
			return false;
		}
		pattern = Pattern.compile(numRegex);
		matcher = pattern.matcher(password);

		pattern = Pattern.compile(chRegex);
		matcher = pattern.matcher(password);

		return (matcher.find() && matcher.find());
	}

	public static boolean isLegal2(String password) {
		// 输入是否都是字母或者数字的组合，同时字符串要大于或者等于9
		boolean b1 = Pattern.compile("^[A-Za-z0-9]{6,}$").matcher(password).find();
		// 输入是否全是数字
		boolean b2 = Pattern.compile("^[0-9]*$").matcher(password).find();
		// 输入是否全是字母
		boolean b3 = Pattern.compile("^[A-Za-z]+$").matcher(password).find();

		// System.out.println("b1=" + b1 + ";b2=" + b2 + ";b3=" + b3);

		// 当输入的字符是字母与数字的组合而且大于或者等于9位时正确
		if (b1 && !b2 && !b3) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isLegal3(String password) {
		// 输入是否都是字母或者数字的组合，同时字符串要大于或者等于9
		boolean b1 = Pattern.compile("^.{6,}$").matcher(password).find();
//		// 输入是否全是数字
//		boolean b2 = Pattern.compile("^[0-9]*$").matcher(password).find();
//		// 输入是否全是字母
//		boolean b3 = Pattern.compile("^[A-Za-z]+$").matcher(password).find();

		// System.out.println("b1=" + b1 + ";b2=" + b2 + ";b3=" + b3);

		// 当输入的字符是字母与数字的组合而且大于或者等于9位时正确
		if (b1) {
			return true;
		} else {
			return false;
		}
	}
}
