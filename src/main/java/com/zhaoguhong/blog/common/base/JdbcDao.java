package com.zhaoguhong.blog.common.base;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * springjdbc 通用Dao
 * 
 * @author zhaoguhong
 * @date 2018年1月9日
 */
@Repository
public class JdbcDao {
  @Resource
  private JdbcTemplate jdbcTemplate;
  @Resource
  private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  public JdbcTemplate getJdbcTemplate() {
    return jdbcTemplate;
  }

  public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
    return namedParameterJdbcTemplate;
  }

  private static Logger logger = LoggerFactory.getLogger(JdbcDao.class);

  public List<Map<String, Object>> find(String sql, Object... args) {
    return getJdbcTemplate().query(sql, args, new ColumnMapRowMapper());
  }

  public <T> List<T> find(String sql, Object[] args, RowMapper<T> rowMapper) {
    return getJdbcTemplate().query(sql, args, rowMapper);
  }

  public <T> List<T> find(String sql, RowMapper<T> rowMapper, Object... args) {
    return getJdbcTemplate().query(sql, rowMapper, args);
  }

  public List<Map<String, Object>> find(String sql, Map<String, ?> paramMap) {
    return getNamedParameterJdbcTemplate().query(sql, paramMap, new ColumnMapRowMapper());
  }

  public <T> List<T> find(String sql, Map<String, ?> paramMap, RowMapper<T> rowMapper) {
    return getNamedParameterJdbcTemplate().query(sql, paramMap, rowMapper);
  }

  public <T> T findUnique(String sql, Object... args) {
    List<Map<String, Object>> results = find(sql, args);
    return singleMapResult(DataAccessUtils.singleResult(results));
  }

  public <T> T findUnique(String sql, Map<String, ?> paramMap) {
    List<Map<String, Object>> results = find(sql, paramMap);
    return singleMapResult(DataAccessUtils.singleResult(results));
  }

  /**
   * springjdbc 原queryForMap方法，结果集校验用的是DataAccessUtils.requiredSingleResult(Collection<T>)方法
   * 如果没查询到会抛异常，此处用singleResult方法校验结果集，如果没有查询到，返回null
   */
  public Map<String, Object> queryForMap(String sql, Object... args) {
    List<Map<String, Object>> results = find(sql, args);
    return DataAccessUtils.singleResult(results);
  }

  public Map<String, Object> queryForMap(String sql, Map<String, ?> paramMap) {
    List<Map<String, Object>> results = find(sql, paramMap);
    return DataAccessUtils.singleResult(results);
  }

  public int update(String sql, Object... args) {
    return getJdbcTemplate().update(sql, args);
  }

  public int update(String sql, Map<String, ?> paramMap) {
    return getNamedParameterJdbcTemplate().update(sql, paramMap);
  }

  /**
   * 校验map是否只包含一组数据，并返回value
   */
  @SuppressWarnings("unchecked")
  private <T> T singleMapResult(Map<String, Object> result) {
    if (result == null) {
      return null;
    }
    if (result.size() != 1) {
      logger.error("返回结果集必须是唯一对象");
      throw new IncorrectResultSizeDataAccessException(1, result.size());
    }
    return (T) result.values().iterator().next();
  }

}
