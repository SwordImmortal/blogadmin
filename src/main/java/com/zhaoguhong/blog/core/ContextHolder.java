package com.zhaoguhong.blog.core;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 项目上下文
 * 
 * @author zhaoguhong
 * @date 2018年1月20日
 */
@Component
public class ContextHolder implements ApplicationContextAware {
  /**
   * 存储当前线程当中开启的Hibernate Session
   */
  private static final ThreadLocal<Session> hibernateSessionThreadLocal = new ThreadLocal<Session>();

  private static ApplicationContext applicationContext;
  
  public static final String LOGIN_USER = "LOGIN_USER";

  public static ApplicationContext getApplicationContext() {
    return applicationContext;
  }
  
  /**
   * 获取当前线程的request
   * @return
   */
  public static HttpServletRequest getRequest(){
    return ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
  }
  
  /**
   * 获取当前线程的response
   * @return
   */
  public static HttpServletResponse getResponse(){
    return ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getResponse();
  }
  
  /**
   * 获取HttpSession
   * 
   * @return
   */
  public static HttpSession getHttpSession() {
    return getRequest().getSession();
  }

  public static void setAttribute(String key, Serializable entity) {
    getHttpSession().setAttribute(key, entity);
  }

  public static Object getAttribute(String key) {
    return getHttpSession().getAttribute(key);
  }
  
  @Override
  public void setApplicationContext(ApplicationContext context) {
    Assert.notNull(context, "WebApplicationContext can not be null");
    applicationContext = context;
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

  public static void clean() {
    Session session = hibernateSessionThreadLocal.get();
    if (session != null && session.isOpen()) {
      session.flush();
      session.close();
    }
    hibernateSessionThreadLocal.remove();
  }
}
