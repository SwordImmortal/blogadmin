package com.zhaoguhong.blog.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * 通用工具类
 * 
 * @author zhaoguhong
 * @date 2018年1月20日
 */
public class Common {

  /**
   * 以逗号分隔字符串，转为Integer List
   */
  public static List<Integer> spiltStrToIntegerList(String str) {
    List<Integer> resulst = new ArrayList<Integer>();
    if (StringUtils.isBlank(str)) {
      return resulst;
    }
    String[] array = str.trim().split(",");
    for (String a : array) {
      resulst.add(Integer.valueOf(a.trim()));
    }
    return resulst;
  }

  /**
   * 以逗号分隔字符串，转为Long List
   */
  public static List<Long> spiltStrToLongList(String str) {
    List<Long> resulst = new ArrayList<Long>();
    if (StringUtils.isBlank(str)) {
      return resulst;
    }
    String[] array = str.trim().split(",");
    for (String a : array) {
      resulst.add(Long.valueOf(a.trim()));
    }
    return resulst;
  }
}
