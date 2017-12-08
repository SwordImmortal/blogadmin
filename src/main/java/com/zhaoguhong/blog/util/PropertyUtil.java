package com.zhaoguhong.blog.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * properties文件获取工具类
 */
public class PropertyUtil {
  private static final Logger logger = LoggerFactory.getLogger(PropertyUtil.class);
  private static Properties props = new Properties();
  private static final String[] PROPERTIES = new String[] {"config", "jdbc"};
  static {
    logger.info("开始加载properties文件内容.......");
    InputStream in = null;
    try {
      for (String name : PROPERTIES) {
        // 第一种，通过类加载器进行获取properties文件流
        in = PropertyUtil.class.getClassLoader().getResourceAsStream(name + ".properties");
        // 第二种，通过类进行获取properties文件流
        // in = PropertyUtil.class.getResourceAsStream("/jdbc.properties");
        props.load(in);
      }
    } catch (FileNotFoundException e) {
      logger.error("加载properties文件,文件找不到：" + e.getMessage());
    } catch (IOException e) {
      logger.error("加载properties文件出现IOException");
    } finally {
      if (in != null) {
        try {
          in.close();
        } catch (IOException e) {
          logger.error("加载properties文件,文件流关闭出现异常");
        }
      }
    }
    logger.info("加载properties文件内容完成...........");
  }

  public static String getProperty(String key) {
    return props.getProperty(key);
  }

  public static String getProperty(String key, String defaultValue) {
    return props.getProperty(key, defaultValue);
  }

}
