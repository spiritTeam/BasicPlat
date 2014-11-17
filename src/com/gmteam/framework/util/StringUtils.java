package com.gmteam.framework.util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * 字符串工具类
 * @author wh,zhuhua
 */
public class StringUtils {
    /**
     * 判断字符串是否为NULL或者空
     * @param s 所判断的字符串
     * @return 若为NULL或者空返回true，否则返回null
     */
    public static boolean isNullOrEmpty(String s) {
        return s==null||s.length()==0;
    }

    /**
	 * 判断字符串是否为NULL、空、空格
	 * @param s 所判断的字符串
	 * @return 若为NULL、空、空格返回true，否则返回null
	 */
	public static boolean isNullOrEmptyOrSpace(String s) {
		return isNullOrEmpty(s)||s.trim().length()==0;
	}

	/**
	 * splitString("AABBCCBBDDBBEE", "BB")=["AA","CC","DD","EE"]
	 * @param s1 被分割的字符串
	 * @param s2 作为分割符的字符串 重新实现了string的split方法，建议调用
	 * @return 分割后的字符串数组
	 */
	public static String[] splitString(String s1, String s2) {
		return s1.split(s2);
	}

	/**
	 * 将字符串从一种编码转换成另一种编码
	 * 
	 * @param s
	 * @param strOldEncoding
	 * @param strNewEncoding
	 * @return
	 */
	public static String convertString(String s, String strOldEncoding, String strNewEncoding) {
		if (isNullOrEmpty(s)) return null;
		try {
			byte[] b = s.getBytes(strOldEncoding);
			String s1 = new String(b, strNewEncoding);
			return s1;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 字符串数组转换为LIST
	 * 
	 * @param strArr
	 * @return
	 */
	public static List<String> strArrConvertList(String[] strArr) {
	    if (strArr==null) return null;
		List<String> list = new ArrayList<String>();
		for (String str : strArr) {
			list.add(str);
		}
		return list;
	}

	/**
	 * 字符串转换成LIST
	 * @param str 被转换的字符串
	 * @param regex 分割字符
	 * @return 转换后的list
	 */
	public static List<String> strConvertList(String str, String regex) {
		String[] strArr = splitString(str, regex);
		List<String> list = strArrConvertList(strArr);
		return list;
	}

	/**
	 * 
	 * @param str
	 */
	public static String convertLogStr(String str) {
	    return DateUtils.getLocalDefineDate("yyyy-MM-dd HH:mm:ss SSSS")+"|| "+str;
	}
}