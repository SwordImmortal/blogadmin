package com.zhaoguhong.blog.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

public class read {
	@SuppressWarnings("unused")
	public static void main(String[] args) throws IOException {
		System.out.println("/**");
		System.out.println("* @Description ");
		System.out.println("* @author zhao ");
		System.out.println("* @date ");
		System.out.println("*/");
		File f = new File("C:\\Users\\Administrator\\Desktop\\test\\test.txt");
		String[] flag = null;
		try {
			// BufferedReader br = new BufferedReader(new FileReader(f));//
			// 构造一个BufferedReader类来读取文件
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f), "GBk"));
			String s = null;
			String start = null;
			String end = null;
			String menthod = null;
			int mark = 0;
			while ((s = br.readLine()) != null) {// 使用readLine方法，一次读一行
				if (mark == 0) {
					mark++;
					if (s.contains("request")) {
						s = s.replace("request", "");
						s = s.replaceAll(" +", "");
						menthod = s;
						while (s.contains(".")) {
							int index = s.indexOf(".");
							s = s.substring(0, index) + s.substring(index + 1, index + 2).toUpperCase() + s.substring(index + 2);
						}
						s = s.substring(8);
						System.out.println("public class " + s + "Request" + " implements AllinPayModel {");
						System.out.println();
					} else if (s.contains("response")) {
						s = s.replace("response", "");
						s = s.replaceAll(" +", "");
						while (s.contains(".")) {
							int index = s.indexOf(".");
							s = s.substring(0, index) + s.substring(index + 1, index + 2).toUpperCase() + s.substring(index + 2);
						}
						s = s.substring(8);
						System.out.println("public class " + s + "Response" + " implements Serializable {");
						System.out.println();
					} else {
						System.out.println("public class " + s + " implements Serializable {");
						System.out.println();
					}
				} else {
					s = s.replaceAll(" +", " ");
					s = s.replaceFirst(" +", ";");
					s = s.replaceFirst(" +", ";");
					flag = s.split(";");
					if (flag.length == 3) {
						System.out.println("// " + flag[2]);
						System.out.println("@JsonProperty(\"" + flag[0] + "\")");
						while (flag[0].contains("_")) {
							int index = flag[0].indexOf("_");
							flag[0] = flag[0].substring(0, index) + flag[0].substring(index + 1, index + 2).toUpperCase() + flag[0].substring(index + 2);
						}
						System.out.println("private " + flag[1] + " " + flag[0] + ";\n");
					}
				}
			}
			if (menthod != null) {
				System.out.println("public String getMethodName() {");
				System.out.println("	return\"" + menthod + "\";");
				System.out.println("}");
			}
			System.out.println("}");
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
