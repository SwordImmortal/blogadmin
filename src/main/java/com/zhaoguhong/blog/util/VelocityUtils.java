package com.zhaoguhong.blog.util;

import java.io.StringWriter;
import java.util.Map;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

public class VelocityUtils {

  /**
   * 解析动态SQL语句
   * 
   * @param sql
   * @param parameters
   * @return
   * @throws Exception
   */
  public static String parse(String templateName, Map<String, Object> parameters) {
    VelocityContext context = new VelocityContext(parameters);
    StringWriter writer = new StringWriter();
    VelocityEngine velocityEngine = new VelocityEngine();
    Properties p = new Properties();
    VelocityEngine ve = new VelocityEngine();
    ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
    ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
    try {
      ve.init();
      // 获取模板文件
      Template template = ve.getTemplate(templateName, "utf-8");
      template.merge(context, writer);
    } catch (Exception e) {
      e.printStackTrace();
    } 
    return writer.toString();
  }
}
