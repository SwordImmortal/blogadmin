package com.zhaoguhong.blog.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
/**
 * 从文本文档里面读取代码路径，上传到服务器
 * 
 * @author zhaoguhong
 * @date 2017年11月27日
 */
public class SendCode {
	public static void main(String[] args) throws SocketException, IOException {
		File f = new File("C:\\Users\\User\\Desktop\\upload\\uploadFile.txt");
		String result = "";
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));// 构造一个BufferedReader类来读取文件
			String s = null;
			int a = 0;
			while ((s = br.readLine()) != null) {// 使用readLine方法，一次读一行
				if (a == 0) {
					result = s;
					a = 1;
				} else {
					result = result + "," + s;
				}
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		result=result.replaceAll("\\s*", "");
		System.out.println(result);
		// 本地文件路径
		File file = null;
		// 远程主机上的文件路径
		String targetFile = null;
		InputStream input = null;
		// 本机部署后的项目根路径
		String projectPath = "D:/root/html/ddd";
		// 远程主机项目跟路径
		String targetProjectPath = "/root/mnt/www";
		// 要上传的文件路径，以逗号作为分隔符(请使用myeclipse中的Copy Qualified Name功能，确保所选文件路径无误)
		String a[] = result.split(",");
		// 创建ftp客户端
		FTPClient ftpClient = new FTPClient();
		ftpClient.setControlEncoding("GBK");
		// 链接ftp服务器
		ftpClient.connect("192.168.0.1", 11);
		// 登录ftp
		ftpClient.login("root", "root");
		int reply = ftpClient.getReplyCode();
		// 判断文件服务器是否可用,如果reply返回230就算成功了,否则关闭连接
		if (!FTPReply.isPositiveCompletion(reply)) {
			ftpClient.disconnect();
			System.out.println("文件服务器是否不可用");
		} else {
			System.out.println("文件服务器连接成功");
			for (String filePath : a) {
				String d[] = filePath.split("\\.");
				if (d[d.length - 1].equals("java")) {
					file = new File(projectPath + "/WEB-INF/classes" + filePath.substring(12, filePath.length() - 5)
							+ ".class");
					targetFile = targetProjectPath + "WEB-INF/classes" + filePath.substring(12, filePath.length() - 5)
							+ ".class";
				} else if (d[d.length - 1].equals("xml") || d[d.length - 1].equals("properties")) {
					file = new File(projectPath + "/WEB-INF/classes" + filePath.substring(12, filePath.length()));
					targetFile = targetProjectPath + "WEB-INF/classes" + filePath.substring(12, filePath.length());
				} else {
					file = new File(projectPath + filePath.substring(16, filePath.length()));
					targetFile = targetProjectPath + filePath.substring(16, filePath.length());
				}
				// 在远程主机上新建文件夹
				String b[] = targetFile.split("/");
				String c = "";
				for (int i = 1; i < b.length - 1; i++) {
					c = c + "/" + b[i];
					try{
					    Thread thread = Thread.currentThread();
					    thread.sleep(1500);//暂停1.5秒后程序继续执行
					}catch (InterruptedException e) {
					    // TODO Auto-generated catch block
					    e.printStackTrace();
					}
					if (ftpClient.makeDirectory(c)) {
						
						System.out.println("新建文件夹:" + c);
					}
				}
				// 设置文件传输格式
				//ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
				// 读取文件
				input = new FileInputStream(file);
				// 上传文件并指定路径
				if (ftpClient.storeFile(targetFile, input)) {
					System.out.println("上传成功" + file + "------->" + targetFile);
				/*	try{
					    Thread thread = Thread.currentThread();
					    thread.sleep(1500);//暂停1.5秒后程序继续执行
					}catch (InterruptedException e) {
					    // TODO Auto-generated catch block
					    e.printStackTrace();
					}*/
				} else {
					System.err.println(targetFile + "上传失败");
				}
				input.close();
			}
		}
		ftpClient.logout();
		if (ftpClient.isConnected())
			ftpClient.disconnect();
		System.err.println("结束！");
	}
}
