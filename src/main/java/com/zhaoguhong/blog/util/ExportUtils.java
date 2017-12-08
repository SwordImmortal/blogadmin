package com.zhaoguhong.blog.util;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.MapUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("serial")
public class ExportUtils {

  private static Logger logger = LoggerFactory.getLogger(ExportUtils.class);

  public void export(Map<String, Object> param, HttpServletResponse response) {
    String tableName = MapUtils.getString(param, "tableName");
    String[] rowName = (String[]) param.get("rowName");
    String[] propertys = (String[]) param.get("propertys");
    List<Map<String, Object>> datas = (List<Map<String, Object>>) param.get("datas");

    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    HSSFWorkbook workbook = new HSSFWorkbook(); // 创建工作簿对象
    HSSFSheet sheet = workbook.createSheet(tableName); // 创建工作表
    // 初始化数据
    String cellValue = null;
    HSSFRow row = null;
    HSSFCellStyle style = this.getStyle(workbook); // 单元格样式对象
    HSSFCellStyle style_left = this.getStyle_left(workbook); // 单元格样式对象
    // 定义所需列数
    int columnNum = rowName.length;
    HSSFRow rowRowName = sheet.createRow(0);
    rowRowName.setHeight((short) 400);// 设置行高

    // 将列头设置到sheet的单元格中
    for (int n = 0; n < columnNum; n++) {
      HSSFCell cellRowName = rowRowName.createCell(n); // 创建列头对应个数的单元格
      cellRowName.setCellType(HSSFCell.CELL_TYPE_STRING); // 设置列头单元格的数据类型
      HSSFRichTextString text = new HSSFRichTextString(rowName[n]);
      cellRowName.setCellValue(text); // 设置列头单元格的值
      cellRowName.setCellStyle(getSeColumnTopStyle(workbook)); // 设置列头单元格样式
    }
    for (int i = 0; i < datas.size(); i++) {
      Map<String, Object> map = datas.get(i);
      row = sheet.createRow(i + 1);
      row.setHeight((short) 300);// 设置行高
      int j = 0;
      for (String property : propertys) {
        HSSFCell cell = row.createCell(j++, HSSFCell.CELL_TYPE_STRING);
        cell.setCellStyle(style);
        cell.setCellValue(MapUtils.getString(map, property));
      }
    }

    // 列宽自适应，同时设置列宽的最大值和最小值
    for (int i = 0; i < columnNum; i++) {
      sheet.autoSizeColumn(i);
      if (sheet.getColumnWidth(i) < 7 * 256)
        sheet.setColumnWidth(i, 7 * 256);
      if (sheet.getColumnWidth(i) > 45 * 256)
        sheet.setColumnWidth(i, 45 * 256);
    }
    try {
      String fileName = tableName + df.format(new Date()).substring(0, 10) + ".xls";
      fileName = new String(fileName.getBytes("gb2312"), "iso8859-1");
      response.setHeader("Content-disposition", "attachment;filename=" + fileName);
      OutputStream ouputStream = response.getOutputStream();
      workbook.write(ouputStream);
      ouputStream.flush();
      ouputStream.close();
    } catch (Exception e) {
      logger.info("导出Excel出错！");
    }
  }

  /*
   * 列数据信息单元格样式 靠左对齐
   */
  public HSSFCellStyle getStyle_left(HSSFWorkbook workbook) {
    // 设置字体
    HSSFFont font = workbook.createFont();
    // 设置字体大小
    font.setFontHeightInPoints((short) 11);
    // 设置字体名字
    font.setFontName("Courier New");
    // 设置样式;
    HSSFCellStyle style = workbook.createCellStyle();
    // 设置底边框;
    style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
    // 设置底边框颜色;
    style.setBottomBorderColor(HSSFColor.BLACK.index);
    // 设置左边框;
    style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
    // 设置左边框颜色;
    style.setLeftBorderColor(HSSFColor.BLACK.index);
    // 设置右边框;
    style.setBorderRight(HSSFCellStyle.BORDER_THIN);
    // 设置右边框颜色;
    style.setRightBorderColor(HSSFColor.BLACK.index);
    // 设置顶边框;
    style.setBorderTop(HSSFCellStyle.BORDER_THIN);
    // 设置顶边框颜色;
    style.setTopBorderColor(HSSFColor.BLACK.index);
    // 在样式用应用设置的字体;
    style.setFont(font);
    // 设置自动换行;
    style.setWrapText(false);
    // 设置垂直对齐的样式为居中对齐;
    style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
    return style;
  }

  /*
   * 列数据信息单元格样式 居中显示
   */
  public HSSFCellStyle getStyle(HSSFWorkbook workbook) {
    HSSFCellStyle style = getStyle_left(workbook);
    style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
    return style;
  }

  /*
   * 第二列列头单元格样式
   */
  public HSSFCellStyle getSeColumnTopStyle(HSSFWorkbook workbook) {
    // 设置字体
    HSSFFont font = workbook.createFont();
    // 设置字体大小
    font.setFontHeightInPoints((short) 12);
    // 字体加粗
    font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
    // 设置字体名字
    font.setFontName("Courier New");
    HSSFCellStyle style = getStyle(workbook);
    style.setFont(font);
    return style;
  }


}
