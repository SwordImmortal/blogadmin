package com.zhaoguhong.util;

import java.io.IOException;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.junit.Test;

import com.google.common.collect.Maps;
import com.zhaoguhong.blog.util.HttpUtil;
import com.zhaoguhong.blog.util.PropertyUtil;

/**
 * 微信发送测试
 * 
 * @author zhaoguhong
 * @date 2017年12月12日
 */
public class WeChatTest {
  // 一对一
  private static String sendkey = PropertyUtil.getProperty("sendkey");
  // 一对多，测试key
  private static String sendMoney = PropertyUtil.getProperty("sendmoneykey2");

  /**
   * 微信发送一对一
   */
  @Test
  public void testSend() throws ClientProtocolException, IOException {
    String url = "https://sc.ftqq.com/";
    Map<String, Object> params = Maps.newHashMap();
    params.put("text", "测试一下哈");
    params.put("desp", "今天天气很好啊");
    System.out.println(HttpUtil.postMap(url + sendkey + ".send", params));
  }

  /**
   * 微信发送一对多
   */
  @Test
  public void testMoney() throws ClientProtocolException, IOException {
    String url = "https://pushbear.ftqq.com/sub";
    Map<String, Object> params = Maps.newHashMap();
    params.put("sendkey", sendMoney);
    params.put("text", "测试一下哈");
    params.put("desp", "今天双十二了");
    System.out.println(HttpUtil.postMap(url, params));
  }
}
