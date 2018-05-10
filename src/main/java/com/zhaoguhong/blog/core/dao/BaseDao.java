package com.zhaoguhong.blog.core.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.zhaoguhong.blog.common.base.BaseEntity;
import com.zhaoguhong.blog.common.base.Page;
import com.zhaoguhong.blog.core.ContextHolder;

/**
 * hibernate DAO
 * 
 * @author zhaoguhong
 * @date 2018年1月20日
 */
@Repository
public class BaseDao {
  @Resource
  private SessionFactory sessionFactory;

  public SessionFactory getSessionFactory() {
    return sessionFactory;
  }

  /**
   * 获取session，如果有事务，取当前session 如果没有事务，获取当前线程中的session
   * 
   * @return session 实例
   */
  public Session getSession() {
    if (StringUtils.isEmpty(TransactionSynchronizationManager.getCurrentTransactionName())) {
      Session session = ContextHolder.getHibernateSession();
      if (session != null && session.isOpen()) {
        return session;
      }
      session = sessionFactory.openSession();
      ContextHolder.setHibernateSession(session);
      return session;
    }
    return sessionFactory.getCurrentSession();
  }

  @Transactional
  public void save(Object o) {
    getSession().saveOrUpdate(o);
  }

  @SuppressWarnings("unchecked")
  public <T> T getObject(Class clazz, Serializable id) {
    return (T) getSession().get(clazz, id);
  }


  @SuppressWarnings("unchecked")
  public <T> List<T> find(String hql, Object... parameters) {
    return createQuery(hql, parameters).list();
  }

  @SuppressWarnings("unchecked")
  public <T> List<T> find(String hql, Map<String, ?> parameters) {
    return createQuery(hql, parameters).list();
  }

  public <T> List<T> findAll(Class<T> cls) {
    String hql = "from " + cls.getName();
    if (BaseEntity.class.isAssignableFrom(cls)) {
      hql += " where isDeleted = 0 order by createDt desc";
    }
    return find(hql);
  }

  public <T> Page<T> find(Page<T> page, String hql, Object... parameters) {
    Query query = createQuery(hql, parameters);
    long totalCount = findUnique(generateCountHql(hql), parameters);
    page.setEntityCount((int) totalCount);
    query.setFirstResult(page.getFirstEntityIndex());
    query.setMaxResults(page.getPageSize());
    page.setEntityList(query.list());
    return page;
  }

  public <T> Page<T> find(Page<T> page, String hql, Map<String, ?> parameters) {
    Query query = createQuery(hql, parameters);
    long totalCount = findUnique(generateCountHql(hql), parameters);
    page.setEntityCount((int) totalCount);
    query.setFirstResult(page.getFirstEntityIndex());
    query.setMaxResults(page.getPageSize());
    page.setEntityList(query.list());
    return page;
  }

  public <T> T findUnique(String hql, Object... parameters) {
    return (T) createQuery(hql, parameters).uniqueResult();
  }

  public <T> T findUnique(String hql, Map<String, ?> parameters) {
    return (T) createQuery(hql, parameters).uniqueResult();
  }

  public Query createQuery(String hql, Object... parameters) {
    Query query = getSession().createQuery(hql);
    if (parameters != null) {
      for (int i = 0; i < parameters.length; i++) {
        query.setParameter(i, parameters[i]);
      }
    }
    return query;
  }

  public Query createQuery(String queryString, Map<String, ?> parameters) {
    Query query = getSession().createQuery(queryString);
    if (parameters != null) {
      query.setProperties(parameters);
    }
    return query;
  }

  /**
   * 拼接查询数量hql
   */
  protected String generateCountHql(String hql) {
    hql = StringUtils.substringAfter(hql, "from");
    hql = StringUtils.substringAfterLast(hql, "order by");
    return "select count(*)  from " + hql;
  }

}
