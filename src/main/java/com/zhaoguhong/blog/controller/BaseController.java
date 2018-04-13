package com.zhaoguhong.blog.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class BaseController {

  @RequestMapping("/")
  public String index() {
    return "redirect:/admin/index.html";
  }
  protected Map<String, Object> successResult() {
    Map<String, Object> successMap = new HashMap<>(2);
    successMap.put("status", true);
    return successMap;
  }
  protected Map<String, Object> successResult(String msg) {
    Map<String, Object> successMap = new HashMap<>(2);
    successMap.put("status", true);
    successMap.put("msg", msg);
    return successMap;
  }

  protected Map<String, Object> errorResult(String msg) {
    Map<String, Object> errorMap = new HashMap<>(2);
    errorMap.put("status", false);
    errorMap.put("msg", msg);
    return errorMap;
  }

}
