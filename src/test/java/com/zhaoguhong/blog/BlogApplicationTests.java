package com.zhaoguhong.blog;


import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.zhaoguhong.blog.common.base.BaseRepositoryFactoryBean;
import com.zhaoguhong.blog.dao.UserDao;
import com.zhaoguhong.blog.entity.User;

@RunWith(SpringRunner.class)
@SpringBootTest
// @EnableTransactionManagement
// 添加@EnableTransactionManagement注解，该注解启用了注解式事务管理 <tx:annotation-driven
// />，这样在方法上的@Transactional注解就起作用了，但是实际测试中不加这句，@Transactional注解依然有用
@EnableJpaRepositories(basePackages = {"com.zhaoguhong.blog.*"},
    repositoryFactoryBeanClass = BaseRepositoryFactoryBean.class)
public class BlogApplicationTests {
  private Logger logger = LoggerFactory.getLogger(this.getClass());
  @Resource
  private UserDao userDao;

  @Test
  @Transactional
  public void testJpa() {
    logger.info("------------------------------------------------------");
    System.out.println(userDao.findAll());
    System.out.println(userDao.findOne(1L));
    User user = new User();
    user.setUserName("qqq");
    user.setPassword("");
    user.setCreateBy(1L);
    try {
      userDao.save(user);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      throw new RuntimeException();

    }
    System.out.println(userDao.getOne(1L));
    System.out.println("--------------------" + userDao.getOne((long) 1) + "---------------------------------");
    // logger.info("test:"+userDao.saveEntity(user));
  }

}
