package com.zhaoguhong.blog.dao;

import org.springframework.stereotype.Repository;

import com.zhaoguhong.blog.core.dao.BaseDao;
import com.zhaoguhong.blog.entity.User;

@Repository
public class UserDao extends BaseDao {
  
  public User findUserByUserName(String userName) {
    String hql = "from User where isDeleted = 0 and userName =?";
    return findUnique(hql, userName);
  }
  
}
