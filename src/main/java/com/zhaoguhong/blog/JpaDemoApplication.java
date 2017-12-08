package com.zhaoguhong.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.zhaoguhong.blog.common.base.BaseRepositoryFactoryBean;

@EnableJpaRepositories(basePackages = {"com.zhaoguhong.blog.*"},
    repositoryFactoryBeanClass = BaseRepositoryFactoryBean.class)
@SpringBootApplication
public class JpaDemoApplication extends WebMvcConfigurerAdapter {
  /**
   * 1、 extends WebMvcConfigurationSupport 2、重写下面方法; setUseSuffixPatternMatch :
   * 设置是否是后缀模式匹配，如“/user”是否匹配/user.*，默认真即匹配； setUseTrailingSlashMatch :
   * 设置是否自动后缀路径模式匹配，如“/user”是否匹配“/user/”，默认真即匹配；
   */
  @Override
  public void configurePathMatch(PathMatchConfigurer configurer) {
    super.configurePathMatch(configurer);
    configurer.setUseSuffixPatternMatch(false)
        .setUseTrailingSlashMatch(true);
  }

  public static void main(String[] args) {
    SpringApplication.run(JpaDemoApplication.class, args);
    System.out.println("启动成功");
  }
}
