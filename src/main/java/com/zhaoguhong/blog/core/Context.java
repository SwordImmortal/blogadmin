package com.zhaoguhong.blog.core;

import org.hibernate.Session;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.context.WebApplicationContext;

/**
 * 项目上下文
 * 
 * @author zhaoguhong
 * @date 2018年1月20日
 */
@Component
public class Context implements ApplicationContextAware {
  /**
   * 存储当前线程当中开启的Hibernate Session
   */
  private static final ThreadLocal<Session> hibernateSessionThreadLocal = new ThreadLocal<Session>();

  private static ApplicationContext applicationContext;

  public static ApplicationContext getApplicationContext() {
    return applicationContext;
  }
  
  @Override
  public void setApplicationContext(ApplicationContext context) {
    Assert.notNull(context, "WebApplicationContext can not be null");
    applicationContext = (WebApplicationContext) context;
  }

  @SuppressWarnings("unchecked")
  public static <T> T getBean(String beanId) {
    return (T) applicationContext.getBean(beanId);
  }
  public static Session getHibernateSession() {
    return hibernateSessionThreadLocal.get();
  }

  public static void setHibernateSession(Session session) {
    hibernateSessionThreadLocal.set(session);
  }

  public static ThreadLocal<Session> getHibernateSessionThreadLocal() {
    return hibernateSessionThreadLocal;
  }

  public static void closeHibernateSession() {
    Session session = hibernateSessionThreadLocal.get();
    if (session != null) {
      if (session.isOpen()) {
        session.flush();
        session.close();
      }
    }
    hibernateSessionThreadLocal.remove();
  }
}
