package com.zhaoguhong.util;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zhaoguhong.blog.util.JSONUtil;

/**
 * 路由器测试
 * 
 * @author zhaoguhong
 * @date 2017年12月12日
 */
public class RouterTest {
  public Logger logger = (Logger) LoggerFactory.getLogger(getClass());

  @Test
  public void testIp() throws ClientProtocolException, IOException {
    HttpGet httpGet = new HttpGet("http://192.168.0.1/goform/SpeedControlInit");
    HttpClient httpClient = HttpClients.createDefault();
    HttpResponse response = httpClient.execute(httpGet);
    // 获取Response对象的Entity
    HttpEntity entity = response.getEntity();
    if (entity != null) {
      // 响应内容
      String content = EntityUtils.toString(entity, "utf-8");
      Map<String, Object> map = JSONUtil.toMap(content);
      List<Map<String, String>> controllist = (List<Map<String, String>>) map.get("controllist");
      System.out.println("当前在线设备数量：" + controllist.size() + "个");
      for (Map<String, String> control : controllist) {
        String ip = control.get("ipval");
        String mac = control.get("macval");
        String hostname = control.get("hostname");
        while (hostname.length() < 24) {
          hostname += " ";
        }
        System.out.println("设备名称：" + hostname + "\t" + "ip地址：" + ip + "\t" + "mac地址:：" + mac);
      }
    }
  }

}
