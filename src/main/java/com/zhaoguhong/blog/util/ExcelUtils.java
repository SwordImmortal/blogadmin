package com.zhaoguhong.blog.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExcelUtils {

  private static Logger logger = LoggerFactory.getLogger(ExportUtils.class);


  public static Map<Integer, String> generateReMap() {
    String s = "A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z";
    String[] array = s.split(",");
    Map<Integer, String> map = new HashMap<Integer, String>();
    for (int i = 0; i < array.length; i++) {
      map.put(i, array[i]);
    }
    return map;
  }

  /**
   * 数字转excel列号
   * 
   * @param column
   * @return
   */
  public static String converColumnNumber(Integer number) {
    Map<Integer, String> map = generateReMap();
    String columnName = "";
    int i = number / 26;
    int remainder = number % 26;
    String prefix = map.get(i - 1);
    String suffix = map.get(remainder);
    if (StringUtils.isNotEmpty(prefix)) {
      columnName = prefix + suffix;
    } else {
      columnName = suffix;
    }
    return columnName;
  }

  /**
   * excel列号转数字
   * 
   * @param column
   * @return
   */
  public static int converColumnNumber(String column) {
    if (!column.matches("[A-Z]+")) {
      throw new IllegalArgumentException("错误的excel列号");
    }
    int colNumber = 0;
    char[] chars = column.toUpperCase().toCharArray();
    for (int i = 0; i < chars.length; i++) {
      colNumber += ((int) chars[i] - (int) 'A' + 1)
          * (int) Math.pow(26, chars.length - i - 1);
    }
    return colNumber - 1;
  }

  public static String getCellValue(Cell cell) {
    String ret = "";
    if (cell == null) {
      return ret;
    }
    switch (cell.getCellType()) {
      case Cell.CELL_TYPE_BLANK:
        ret = "";
        break;
      case Cell.CELL_TYPE_BOOLEAN:
        ret = String.valueOf(cell.getBooleanCellValue());
        break;
      case Cell.CELL_TYPE_ERROR:
        ret = null;
        break;
      case Cell.CELL_TYPE_FORMULA:
        Workbook wb = cell.getSheet().getWorkbook();
        CreationHelper crateHelper = wb.getCreationHelper();
        FormulaEvaluator evaluator = crateHelper.createFormulaEvaluator();
        ret = getCellValue(evaluator.evaluateInCell(cell));
        break;
      case Cell.CELL_TYPE_NUMERIC:
        if (DateUtil.isCellDateFormatted(cell)) {
          Date theDate = cell.getDateCellValue();
          DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
          ret = format.format(theDate);
        } else {
          ret = NumberToTextConverter.toText(cell.getNumericCellValue());
        }
        break;
      case Cell.CELL_TYPE_STRING:
        ret = cell.getRichStringCellValue().getString();
        break;
      default:
        ret = "";
    }

    return ret;
  }

  /**
   * 读取file为excel
   * 
   * @param file
   * @return
   */
  public static Workbook getWookBook(String path) {
    if (path == null || !(path.endsWith(".xls") || path.endsWith(".xlsx"))) {
      throw new RuntimeException("文件不是标准的excel格式");
    }
    Workbook workbook = null;
    InputStream inputStream = null;
    try {
      inputStream = new FileInputStream(new File(path));
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e.getMessage());
    }
    try {
      if (path.endsWith(".xls")) {
        workbook = new HSSFWorkbook(inputStream);
      } else if (path.endsWith(".xlsx")) {
        workbook = new XSSFWorkbook(inputStream);
      }
    } catch (Exception e) {
      throw new RuntimeException("文件不是标准的excel格式");
    } finally {
      if (inputStream != null) {
        try {
          inputStream.close();
        } catch (IOException e) {
          logger.warn("关闭流异常！");
        }
      }
    }
    return workbook;
  }

  public static void main(String[] args) {
    // Integer a = 130;
    System.out.println(converColumnNumber("AB"));
  }

}
