package com.zhaoguhong.blog.controller;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zhaoguhong.blog.service.CodehelperService;
import com.zhaoguhong.blog.util.GenerateEntityUtil;
import com.zhaoguhong.blog.util.SQLFormatter;

/**
 * Codehelper Controller
 * 
 * @author zhaoguhong
 *
 */
@RestController
public class CodehelperController {
  @Resource
  private CodehelperService codehelperService;
  private Logger logger = LoggerFactory.getLogger(this.getClass());

  /**
   * 根据表名获得实体类
   */
  @RequestMapping("/getEntity")
  @Transactional
  public String getEntity(@RequestParam Map<String, Object> map) {
    logger.info("生成实体类：" + map);
    return GenerateEntityUtil.getEntity(map);
  }

  /**
   * 根据表名获得select SQl
   */
  @RequestMapping("/getSelectSql")
  public String getSelectSql(String tableName) {
    return codehelperService.getSelectSql(tableName);
  }

  /**
   * 根据表名获得Mapper
   */
  @RequestMapping("/getMapper")
  public String getMapper(String tableName, boolean checkNull) {
    return codehelperService.getMapper(tableName, checkNull);
  }

  /**
   * 格式化SQL
   */
  @RequestMapping(value = "/formatSql", produces = "text/plain;charset=UTF-8")
  @Transactional
  public String formatSql(String sql) {
    logger.info("格式化SQL：" + sql);
    sql = new SQLFormatter().format(sql);
    if (sql.startsWith("\n")) {
      sql = sql.substring(1);
    }
    return sql;
  }

}
