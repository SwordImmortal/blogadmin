package com.zhaoguhong.blog.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanMap;

public class BeanUtils {

  /**
   * 对象转map

   */
  public Map<String, Object> objectToMap(Object bean) {
    Map<String, Object> resultMap = new HashMap<String, Object>();
    if (bean != null) {
      Map<Object, Object> beanMap = new BeanMap(bean);
      for (Map.Entry<Object, Object> entry : beanMap.entrySet()) {
        resultMap.put((String) entry.getKey(), entry.getValue());
      }
    }
    return resultMap;
  }
}
