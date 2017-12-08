package com.zhaoguhong.blog.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class BaseController {
  private Logger logger = LoggerFactory.getLogger(getClass());

  @RequestMapping("/")
  public String index() {
    return "redirect:/admin/index.html";
  }

}
