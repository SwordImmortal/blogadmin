package com.zhaoguhong.blog.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;

public class DateUtil {
  private static String[] parsePatterns = {"yyyy-MM-dd",
      "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM", "yyyy/MM/dd",
      "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM", "yyyy.MM.dd",
      "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

  /**
   * 转换String为date类型
   * 
   * @param str
   * @return
   */
  public static Date parseDate(String str) {
    Date date = null;
    try {
      date = DateUtils.parseDate(str, parsePatterns);
    } catch (ParseException e) {
      throw new RuntimeException(e.getMessage());
    }
    return date;
  }

  public static void main(String[] args) {
    System.out.println(DateFormat.getDateTimeInstance().format(parseDate("2012-3-3")));
  }
}
