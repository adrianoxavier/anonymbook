package com.anonymbook.server.util;

public class Conversor {

	public static String encodeUnicode(String str) {
		StringBuilder sb = new StringBuilder();
		char[] chars = str.toCharArray();
		for (char ch : chars) {
			if (' ' <= ch && ch <= '\u007E')
				sb.append(ch);
			else
				sb.append(String.format("'\\u%04x'", ch & 0xFFFF));
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		System.out.println(encodeUnicode("Faça"));
	}
}
