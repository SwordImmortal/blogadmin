package com.zhaoguhong.blog.controller;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zhaoguhong.blog.core.ContextHolder;
import com.zhaoguhong.blog.dao.UserDao;
import com.zhaoguhong.blog.entity.User;

/**
 * 用户 Controller
 * 
 * @author zhaoguhong
 * @date 2018年4月11日
 */
@RestController
@RequestMapping("/user")
public class UserController extends BaseController {
  @Resource
  private UserDao userDao;

  /**
   * 用户登录
   * 
   * @param params
   * @return
   */
  @RequestMapping("/login")
  public Map<String, Object> login(@RequestBody Map<String, Object> params) {
    String userName = MapUtils.getString(params, "userName");
    String password = MapUtils.getString(params, "password");
    if (StringUtils.isBlank(userName)) {
      return errorResult("用户名不能为空！");
    }
    if (StringUtils.isBlank(password)) {
      return errorResult("密码不能为空！");
    }
    User user = userDao.findUserByUserName(userName);
    if (user == null) {
      return errorResult("该用户不存在！");
    }
    if (!password.equals(user.getPassword())) {
      return errorResult("密码错误！");
    }
    ContextHolder.setAttribute(ContextHolder.LOGIN_USER, user);
    return successResult();
  }

}
