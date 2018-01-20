package com.zhaoguhong.blog.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.vendor.HibernateJpaSessionFactoryBean;

/**
 * SpringConfig
 * 
 * @author zhaoguhong
 * @date 2018年1月20日
 */
@Configuration
public class SpringConfig {

  /**
   * Hibernate SessionFactory config
   * 
   * @return
   */
  @Bean
  public HibernateJpaSessionFactoryBean sessionFactory() {
    return new HibernateJpaSessionFactoryBean();
  }
}
