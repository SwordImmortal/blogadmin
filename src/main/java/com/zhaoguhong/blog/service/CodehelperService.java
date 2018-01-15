package com.zhaoguhong.blog.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.zhaoguhong.blog.util.DbHelper;
import com.zhaoguhong.blog.util.DbUtil;
import com.zhaoguhong.blog.util.VelocityUtils;

@Service
public class CodehelperService {
  Logger logger = LoggerFactory.getLogger(CodehelperService.class);
  public static Set<String> baseFileds = new HashSet<String>();
  static {
    baseFileds.add("IS_DELETED");
    baseFileds.add("CREATE_BY");
    baseFileds.add("CREATE_DT");
    baseFileds.add("UPDATE_BY");
    baseFileds.add("UPDATE_DT");
    baseFileds.add("MIMIC_BY");
    baseFileds.add("PROXY_BY");
  }

  /**
   * 根据表名获取sql
   * 
   * @param tableName
   * @return
   */
  @Transactional
  public String getSelectSql(String tableName) {
    StringBuilder sql = new StringBuilder();
    Connection conn = DbUtil.openConnection(); // 得到数据库连接
    DbHelper dbHelper = new DbHelper(conn);
    List<String> colNames = dbHelper.getColNames(tableName);// 列名
    List<String> fieldNames = dbHelper.getFieldNames(tableName);// 字段名
    List<String> comments = dbHelper.getComments(tableName);// 注释
    int size = colNames.size();
    try {
      sql.append("-- 查询\r\n");
      String strsql = "select * from " + tableName;
      sql.append("SELECT ");
      for (int i = 0; i < size; i++) {
        sql.append(colNames.get(i) + " " + fieldNames.get(i));
        if (i != size - 1) {
          sql.append(", ");
        }
      }
      sql.append(" FROM " + tableName + " WHERE IS_DELETED=0 ORDER BY " + fieldNames.get(0) + " DESC");
      sql.append("\n\n-- 添加字段\r\n");
      sql.append("ALTER TABLE `" + tableName
          + "` ADD COLUMN `DEMO` VARCHAR (100) DEFAULT NULL COMMENT '添加字段';");
      sql.append("\n\n-- 修改字段\r\n");
      sql.append("ALTER TABLE `" + tableName
          + "` MODIFY COLUMN `DEMO` VARCHAR (100) DEFAULT NULL COMMENT '修改字段';");
      // -- 不带注释
      sql.append("\n\n-- insert 不带注释\r\n");
      sql.append("INSERT INTO " + tableName
          + " ( ");
      for (int i = 0; i < size; i++) {
        sql.append(colNames.get(i));
        if (i != size - 1) {
          sql.append(", ");
        }
      }
      sql.append(") VALUES ");
      sql.append("(");
      for (int i = 0; i < size; i++) {
        sql.append("?");
        if (i != size - 1) {
          sql.append(", ");
        }
      }
      sql.append(");");

      // -- insert 带注释
      sql.append("\n\n-- insert 带注释\r\n");
      sql.append("INSERT INTO " + tableName
          + " (\r\n");
      for (int i = 0; i < size; i++) {
        sql.append("\t" + colNames.get(i));
        sql.append(i == size - 1 ? " " : ",");
        // 如果有注释
        if (StringUtils.isNotBlank(comments.get(i))) {
          if (colNames.get(i).length() >= 27) {
            sql.append("\t-- " + comments.get(i));
          } else {
            for (int j = 0; j < 27 - colNames.get(i).length(); j++) {
              sql.append(" ");
            }
            sql.append("-- " + comments.get(i));
          }
        }
        sql.append("\r\n");
      }
      sql.append(") VALUES ");
      sql.append("(\r\n");
      for (int i = 0; i < size; i++) {
        sql.append("\t?");
        sql.append(i == size - 1 ? " " : ",");
        sql.append(" -- " + StringUtils.defaultIfBlank(comments.get(i), colNames.get(i)) + "\r\n");
      }
      sql.append(");");
      sql.append("\n\n-- markdown\n\n");
      sql.append(dbHelper.getMarkDown(tableName));
    } catch (Exception e) {
      e.printStackTrace();
      return "SQLException,该表或许不存在！";
    } finally {
      DbUtil.closeDatabase(conn, null, null);
    }
    return sql.toString();
  }

  public String getMapper(String tableName, boolean checkNull) {
    String entityStr = "";
    String strsql = "select * from " + tableName;
    PreparedStatement pstmt = null;
    Connection conn = DbUtil.openConnection(); // 得到数据库连接
    DbHelper dbHelper = new DbHelper(conn);
    try {
      Map<String, Object> parameters = Maps.newHashMap();
      parameters.put("checkNull", checkNull);
      parameters.put("tableName", tableName.contains(".") ? StringUtils.substringAfter(tableName, ".") : tableName);// 表名
      parameters.put("entityObjName", DbUtil.sqlField2JavaField(MapUtils.getString(parameters, "tableName")));// 数据库列名
      parameters.put("entityName", DbUtil.toFirstUpper(MapUtils.getString(parameters, "entityObjName")));// 实体对象名称
      parameters.put("colNames", dbHelper.getColNames(tableName));// 数据库列名
      parameters.put("fieldNames", dbHelper.getFieldNames(tableName));// 字段名
      parameters.put("fieldTypes", dbHelper.getFieldTypes(tableName));// 字段类型
      parameters.put("date", DateFormatUtils.ISO_DATE_FORMAT.format(new Date()));// 当前时间
      entityStr = VelocityUtils.parse("mapper.vm", parameters);
      DbUtil.closeDatabase(conn, null, null);
    } catch (Exception e) {
      logger.info(e.getMessage());
      return "SQLException,该表或许不存在！";
    }
    return entityStr;
  }
}
