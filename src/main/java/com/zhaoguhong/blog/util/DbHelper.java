package com.zhaoguhong.blog.util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.google.common.collect.Lists;

/**
 * 数据库操作辅助类
 * 
 * @author zhaoguhong
 * @date 2017年5月3日
 */
public class DbHelper {
  private Connection conn;

  public DbHelper(Connection conn) {
    this.conn = conn;
  }

  /**
   * 获取主键
   * 
   * @param tableName
   * @return
   */
  public String getPrimary(String tableName) {
    String primaryName = null;
    try {
      // 获取主键
      DatabaseMetaData dbMetaData = conn.getMetaData();
      ResultSet pkRSet = dbMetaData.getPrimaryKeys(null, null, tableName);
      while (pkRSet.next()) {
        primaryName = Objects.toString(pkRSet.getObject(4));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return primaryName;
  }

  /**
   * 获取列名
   * 
   * @param tableName
   * @return
   */
  public List<String> getColNames(String tableName) {
    List<String> colNames = Lists.newArrayList();// 列名
    String strsql = "select * from " + tableName;
    PreparedStatement pstmt = null;
    try {
      // 获取主键
      pstmt = conn.prepareStatement(strsql);
      ResultSetMetaData rsmd = pstmt.getMetaData();
      int size = rsmd.getColumnCount(); // 共有多少列
      for (int i = 0; i < size; i++) {
        colNames.add(rsmd.getColumnName(i + 1));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return colNames;
  }

  /**
   * 获取字段名
   * 
   * @param tableName
   * @return
   */
  public List<String> getFieldNames(String tableName) {
    List<String> fieldNames = Lists.newArrayList();
    List<String> colNames = getColNames(tableName);
    for (String colName : colNames) {
      fieldNames.add(DbUtil.sqlField2JavaField(colName));
    }
    return fieldNames;
  }

  /**
   * 获取列类型
   * 
   * @param tableName
   * @return
   */
  public List<String> getColTypes(String tableName) {
    List<String> colTypes = Lists.newArrayList();// 列类型
    String strsql = "select * from " + tableName;
    PreparedStatement pstmt = null;
    try {
      // 获取主键
      pstmt = conn.prepareStatement(strsql);
      ResultSetMetaData rsmd = pstmt.getMetaData();
      int size = rsmd.getColumnCount(); // 共有多少列
      for (int i = 0; i < size; i++) {
        colTypes.add(rsmd.getColumnTypeName(i + 1));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return colTypes;
  }

  /**
   * 获取字段类型
   * 
   * @param tableName
   * @return
   */
  public List<String> getFieldTypes(String tableName) {
    List<String> fieldTypes = Lists.newArrayList();
    List<String> colTypes = getColTypes(tableName);
    for (String colType : colTypes) {
      fieldTypes.add(DbUtil.sqlType2JavaType(colType));
    }
    return fieldTypes;
  }

  /**
   * 获取数据库字段注释
   * 
   * @param tableName
   * @return
   */
  public List<String> getComments(String tableName) {
    List<String> commentList = new ArrayList<String>();
    Statement stmt = null;
    ResultSet rs = null;
    try {
      stmt = conn.createStatement();
      rs = stmt.executeQuery("show full columns from " + tableName);
      while (rs.next()) {
        commentList.add(rs.getString("Comment").replaceAll("\"", "'"));
      }
    } catch (SQLException e) {
      throw new RuntimeException("数据库链接异常");
    }
    return commentList;
  }

  /**
   * 获取表注释
   * 
   * @param tableName
   * @return
   */
  public String getTableComment(String tableName) {
    String createSql = getCreateTableSql(tableName);
    String comment = null;
    int index = createSql.indexOf("COMMENT='");
    if (index < 0) {
      return "";
    }
    comment = createSql.substring(index + 9, createSql.length() - 1);
    return comment;
  }

  /**
   * 获得某表的建表语句
   * 
   * @param tableName
   * @return
   */
  public String getCreateTableSql(String tableName) {
    Statement stmt = null;
    ResultSet rs = null;
    try {
      stmt = conn.createStatement();
      rs = stmt.executeQuery("SHOW CREATE TABLE " + tableName);
      if (rs.next()) {
        return rs.getString(2);
      }
    } catch (SQLException e) {
      throw new RuntimeException("数据库链接异常");
    }
    return "";
  }


}
