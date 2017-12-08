package com.zhaoguhong.blog.util;

import org.springframework.web.context.ContextLoader;

/**
 * @author zhaoguhong
 * @date 2017年5月3日
 */
public class ApplicationUtils {

  public static <T> T getBean(Class<T> obj) {
    return (T) ContextLoader.getCurrentWebApplicationContext().getBean(obj);
  }

  @SuppressWarnings("unchecked")
  public static <T> T getBean(String beanName) {
    return (T) ContextLoader.getCurrentWebApplicationContext().getBean(beanName);
  }

}
