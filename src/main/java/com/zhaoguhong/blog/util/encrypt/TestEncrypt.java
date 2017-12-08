//package com.zhaoguhong.blog.util.encrypt;
//
//import java.io.UnsupportedEncodingException;
//
//import org.apache.commons.codec.binary.Base64;
//import org.junit.Test;
//
//public class TestEncrypt {
//  public String str;
//  public String result;
//
//  /**
//   * 替换式加密
//   */
//  @Test
//  public void testReplace() {
//    str = "I love you , do you want to be my girlfriend";
//    str = EncryptUtils.replaceEncrypt(str);
//    println("替换加密后为：" + str);
//    str = EncryptUtils.replaceEncrypt(str);
//    println("替换解密后为：" + str);
//    println("yes ,  I do");
//  }
//
//  /**
//   * 测试异或 两个操作数的位中，相同则结果为0，不同则结果为1
//   */
//  @Test
//  public void testXor() {
//    str = "世界太大，听听自己";
//    int key = 31415926;
//    result = EncryptUtils.xorEncrypt(str, key);
//    println("异或加密后为：" + result);
//    result = EncryptUtils.xorEncrypt(result, key);
//    println("异或解密后为：" + result);
//  }
//
//  /**
//   * base64编码解码
//   * 
//   * @throws UnsupportedEncodingException
//   */
//  @Test
//  public void testBase64() throws UnsupportedEncodingException {
//    str = "你站在桥上看风景，看风景的人在楼上看你";
//    byte[] encodeBase64 = Base64.encodeBase64(str.getBytes("utf-8"));
//
//    // println("Base64编码后为：" + new String(encodeBase64));
//    // println("Base64解码后为：" + new String(Base64.decodeBase64(encodeBase64)));
//
//    println("我的Base64编码后为：" + Base64Utils.encode(str, "utf-8"));
//    println("我的Base64解码后为：" + Base64Utils.decode(Base64Utils.encode(str, "utf-8"), "utf-8"));
//
//  }
//
//  /**
//   * MD5算法具有以下特点： 1、压缩性：任意长度的数据，算出的MD5值长度都是固定的。 2、容易计算：从原数据计算出MD5值很容易。
//   * 3、抗修改性：对原数据进行任何改动，哪怕只修改1个字节，所得到的MD5值都有很大区别。 4、强抗碰撞：已知原数据和其MD5值，想找到一个具有相同MD5值的数据（即伪造数据）是非常困难的。
//   */
//  @Test
//  public void testMd5() {
//    // MD2、 MD4、 MD5、SHA-1 、SHA-256、SHA-512
//    String algorithm = "MD4";
//    str = "梦想是什么，梦想就是一种让你感到坚持就是幸福的东西";
//    result = Md5Util.getMD5(str, algorithm);
//    println(algorithm + "加密后的值为: " + result);
//    println(algorithm + "加密后的值是一个" + result.length() * 4 + "位的二进制，" + result.length() + "位的16进制");
//    // 加盐处理 61
//    // org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices.makeTokenSignature
//  }
//
//  /**
//   * DES加密介绍 DES是一种对称加密算法，所谓对称加密算法即：加密和解密使用相同密钥的算法。
//   * DES加密算法出自IBM的研究，后来被美国政府正式采用，之后开始广泛流传，但是近些年使用越来越少。因为DES使用56位密钥，以现代计算能力，24小时内即可被破解。
//   * 虽然如此，在某些简单应用中，我们还是可以使用DES加密算法
//   */
//  @Test
//  public void testDes() throws Exception {
//    str = "先变成自己喜欢的自己，再遇见一个无需取悦的人。";
//    str = DESUtil.encrypt(str);
//    println("加密后: " + str);
//    str = DESUtil.decrypt(str);
//    println("解密后: " + str);
//  }
//
//  public void println(Object param) {
//    System.out.println(param);
//  }
//
//}
