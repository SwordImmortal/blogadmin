package com.zhaoguhong.blog.util.encrypt;

public class EncryptUtils {

  /**
   * 替换加密解密
   */
  public static String replaceEncrypt(String content) {
    content = content.toLowerCase();
    String str = "abcdefghijklmnopqrstuvwxyz";
    String reverseStr = "zyxwvutsrqponmlkjihgfedcba";
    StringBuilder newContent = new StringBuilder(content.length());
    for (int i = 0; i < content.length(); i++) {
      int index = str.indexOf(content.charAt(i));
      if (index > -1) {
        newContent.append(reverseStr.charAt(index));
      } else {
        newContent.append(content.charAt(i));
      }
    }
    return newContent.toString();
  }

  /**
   * 异或加密
   */
  public static String xorEncrypt(String str, int key) {
    char[] charArray = str.toCharArray();
    for (int i = 0; i < charArray.length; i++) {
      charArray[i] = (char) (charArray[i] ^ key);
    }
    return new String(charArray);
  }

}
