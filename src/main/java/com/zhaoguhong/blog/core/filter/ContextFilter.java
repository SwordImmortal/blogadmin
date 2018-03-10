
package com.zhaoguhong.blog.core.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import com.zhaoguhong.blog.core.ContextHolder;


/**
 * Context过滤器，请求结束，关闭ThreadLocal session
 * 
 * @author zhaoguhong
 * @date 2018年3月10日
 */
@Component
public class ContextFilter extends GenericFilterBean {

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    try {
      chain.doFilter(request, response);
    } finally {
      ContextHolder.clean();
    }

  }
}

