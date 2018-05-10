package com.zhaoguhong.blog.util;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.MapUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Http 工具类
 * 
 * @author zhaoguhong
 * @date 2018年3月10日
 */
public class HttpUtil {

  /**
   * 执行http请求，返回HttpEntity
   */
  public static HttpEntity execute(String uri, Map<String, Object> params, String methodName) {
    // 构建request参数
    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    if (MapUtils.isNotEmpty(params)) {
      params.forEach((key, value) -> {
        nameValuePairs.add(new BasicNameValuePair(key, value.toString()));
      });
    }
    Request request = null;
    if ("GET".equalsIgnoreCase(methodName)) {
      uri += URLEncodedUtils.format(nameValuePairs, "UTF-8");
      request = Request.Get(uri);
    } else {
      request = Request.Post(uri).bodyForm(nameValuePairs, Charset.forName("utf-8"));;
    }
    Executor executor = Executor.newInstance(getHttpClient());
    try {
      Response response = executor.execute(request);
      // 获取返回结果
      HttpResponse httpResponse = response.returnResponse();
      return httpResponse.getEntity();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static CloseableHttpClient getHttpClient() {
    SSLContextBuilder builder = new SSLContextBuilder();
    try {
      builder.loadTrustMaterial(null, new TrustStrategy() {

        @Override
        public boolean isTrusted(java.security.cert.X509Certificate[] chain, String authType)
            throws CertificateException {
          return true;
        }
      });
    } catch (NoSuchAlgorithmException | KeyStoreException e) {
      throw new RuntimeException(e);
    }
    SSLConnectionSocketFactory sslsf = null;
    try {
      sslsf = new SSLConnectionSocketFactory(builder.build(), new NoopHostnameVerifier());
    } catch (KeyManagementException | NoSuchAlgorithmException e1) {
      e1.printStackTrace();
    }
    return HttpClients.custom().setSSLSocketFactory(sslsf).build();
  }

  public static String getString(String url) {
    return getString(url, null);
  }

  /**
   * get请求，返回String
   */
  public static String getString(String url, Map<String, Object> params) {
    try {
      HttpEntity entity = execute(url, params, "get");
      if (entity != null) {
        return EntityUtils.toString(entity, "utf-8");
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return null;
  }

  /**
   * get请求，返回字节数组
   */
  public static byte[] getByteArray(String url, Map<String, Object> params) {
    HttpEntity entity = execute(url, params, "get");
    if (entity != null) {
      try {
        return EntityUtils.toByteArray(entity);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
    return null;
  }

  public static Map<String, Object> postMap(String url, Map<String, Object> params) {
    try {
      HttpEntity entity = execute(url, params, "post");
      if (entity != null) {
        String json = EntityUtils.toString(entity, "UTF-8");
        return JSONUtil.toMap(json);
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return null;
  }

  public void testPost() throws Exception {
    List<NameValuePair> formparams = new ArrayList<NameValuePair>();
    formparams.add(new BasicNameValuePair("param1", "value1"));
    formparams.add(new BasicNameValuePair("param2", "value2"));
    UrlEncodedFormEntity entityParams = new UrlEncodedFormEntity(formparams, "UTF-8");
    HttpPost httpPost = new HttpPost("http://localhost:8081/testPost");
    httpPost.setEntity(entityParams);
    httpPost.addHeader("Authorization", "Bearer " + "access_token");
    HttpClient httpClient = HttpClients.createDefault();
    HttpResponse response = httpClient.execute(httpPost);
    System.out.println("协议版本:" + response.getProtocolVersion());
    System.out.println("状态码:" + response.getStatusLine().getStatusCode());
    System.out.println(response.getStatusLine());
    // 获取Response对象的Entity
    HttpEntity entity = response.getEntity();
    if (entity != null) {
      // 打印响应内容长度
      System.out.println("Response content length: " + entity.getContentLength());
      // 响应内容
      String json = EntityUtils.toString(entity, "UTF-8");
      System.out.println(JSONUtil.toMap(json));
    }
  }

  @SuppressWarnings("unchecked")
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

  public static String getVisitorIp(HttpServletRequest request) {
    String ipAddress = null;
    ipAddress = request.getHeader("x-forwarded-for");
    if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
      ipAddress = request.getHeader("Proxy-Client-IP");
    }
    if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
      ipAddress = request.getHeader("WL-Proxy-Client-IP");
    }
    if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
      ipAddress = request.getRemoteAddr();
      if (ipAddress.equals("127.0.0.1")) {
        // 根据网卡取本机配置的IP
        InetAddress inet = null;
        try {
          inet = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
          e.printStackTrace();
        }
        ipAddress = inet.getHostAddress();
      }
    }
    // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
    if (ipAddress != null && ipAddress.length() > 15) {
      if (ipAddress.indexOf(",") > 0) {
        ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
      }
    }
    return ipAddress;
  }

  public Long uploadFile(MultipartFile multipartFile, HttpServletRequest request) {
    File file = null;
    String fileName = multipartFile.getOriginalFilename();
    try {
      file = File.createTempFile(fileName.substring(0, fileName.lastIndexOf(".")),
          fileName.substring(fileName.lastIndexOf(".")));
      multipartFile.transferTo(file);
      file.deleteOnExit();
    } catch (Exception e) {
      e.printStackTrace();
    }
    CloseableHttpClient httpClient = null;
    Long fileId = null;
    try {
      httpClient = HttpClientBuilder.create().build();
      String urlPath = request.getRequestURL().toString();
      String uripath = request.getRequestURI().toString();
      String serverUrl = urlPath.substring(0, urlPath.indexOf(uripath));
      HttpPost httpPost = new HttpPost(serverUrl);
      MultipartEntityBuilder builder = MultipartEntityBuilder.create();
      builder.addTextBody("fileName", URLEncoder.encode(fileName, "UTF-8"));
      builder.addBinaryBody("file", file);

      builder.addTextBody("modulFrom", "OPEN API");
      builder.addTextBody("fileClass", "102");
      builder.addTextBody("_fileResolver", "uploadFileProcessor#processAnonymous");

      builder.setCharset(Charset.forName("UTF-8"));
      HttpEntity entity = builder.build();
      httpPost.setEntity(entity);
      HttpResponse result = httpClient.execute(httpPost);

      String json = EntityUtils.toString(result.getEntity(), "UTF-8");
      ObjectMapper mapper = new ObjectMapper();
      TypeReference<HashMap<String, Object>> typeRef =
          new TypeReference<HashMap<String, Object>>() {};
      Map<String, Object> jsonResult = mapper.readValue(json, typeRef);
      fileId = MapUtils.getLong(jsonResult, "fileId");
    } catch (IOException ex) {
      throw new RuntimeException("文件上传出错！");
    }
    return fileId;
  }

}
