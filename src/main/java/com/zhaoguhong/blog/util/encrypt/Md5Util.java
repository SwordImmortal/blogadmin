package com.zhaoguhong.blog.util.encrypt;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.junit.Test;

public class Md5Util {
  /**
   * 对字符串md5加密
   *
   * @param str
   * @return
   */
  @Test
  public  static String getMD5(String str) {
    try {
      // 生成一个MD5加密计算摘要
      MessageDigest md = MessageDigest.getInstance("MD5");
      // 计算md5函数
      md.update(str.getBytes());
      // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
      // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
      return new BigInteger(1, md.digest()).toString(16);
    } catch (Exception e) {
      throw new RuntimeException("MD5加密出现错误");
    }
  }
  
  /**
   * 对字符串md5加密
   *
   * @param str
   * @return
   */
  @Test
  public static String getMD5(String str, String algorithm) {
    // 加密之后所得字节数组
    byte[] bytes = null;
    try {
      // 获取MD5算法实例 得到一个md5的消息摘要
      MessageDigest md = MessageDigest.getInstance(algorithm);
      // 添加要进行计算摘要的信息
      md.update(str.getBytes());
      // 得到该摘要
      bytes = md.digest();
      return BytesConvertToHexString(bytes);
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException("找不到该加密算法");
    }
  }

  public static String BytesConvertToHexString(byte[] bytes) {
    StringBuffer sb = new StringBuffer();
    for (byte aByte : bytes) {
      //0xFF表示的是16进制（十进制是255，二进制就是11111111）
      String s = Integer.toHexString(0xff & aByte);
      if (s.length() == 1) {
        sb.append("0" + s);
      } else {
        sb.append(s);
      }
    }
    return sb.toString();
  }


}
