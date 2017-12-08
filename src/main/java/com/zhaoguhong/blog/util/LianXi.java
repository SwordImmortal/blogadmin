package com.zhaoguhong.blog.util;

public class LianXi {
	public static void main(String[] args) {
		fanChuang("1234567890");
	}

	/*
	 * 字符串反串
	 */
	public static void fanChuang(String a){
		StringBuffer sb=new StringBuffer();
		for(int i=a.length()-1;i>=0;i--){
			sb.append(a.charAt(i));
		}
		System.out.print(sb.toString());
	}
}