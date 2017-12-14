package com.zhaoguhong.blog.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.zhaoguhong.blog.dao.BlogDao;
import com.zhaoguhong.blog.entity.Blog;

@RestController
@RequestMapping("/admin")
public class BlogAdminController {
  @Resource
  private BlogDao blogDao;
  private Logger logger = LoggerFactory.getLogger(getClass());

  @RequestMapping("/test")
  public String index() {
    System.out.print("test");
    return "index";
  }

  @RequestMapping("/addBlog")
  public Map<String, Object> addBlog(@RequestParam Map<String, Object> map) {
    Map<String, Object> result = Maps.newHashMap();
    Long id = MapUtils.getLong(map, "id");
    String title = MapUtils.getString(map, "title");
    Long category = MapUtils.getLong(map, "category");
    String content = MapUtils.getString(map, "content");
    if (StringUtils.isAnyBlank(title, content)) {
      result.put("status", false);
      result.put("info", "有必填项为空！");
    }
    Blog blog = (id == null ? new Blog() : blogDao.getOne(id));
    blog.setTitle(title);
    blog.setContent(content);
    blog.setCategory(category);
    if (id == null) {
      // blogDao.saveEntity(blog);
      blog.setCreateDt(new Date());
      blog.setIsDeleted(0);
      blogDao.save(blog);
    } else {
      blogDao.updateEntity(blog);
    }
    result.put("status", true);
    result.put("id", blog.getId());
    return result;
  }

  @RequestMapping("/getBlogs")
  public List<Blog> getBlogs(@RequestParam Map<String, Object> map) {
    List<Blog> blogs = blogDao.findAll();
    for (Blog blog : blogs) {
      if(blog.getContent().trim().length()>20){
        blog.setContent(blog.getContent().trim().substring(0, 20).replace("#", "").replace("&emsp;", "").trim());
      }
    }
    return blogs;
  }

  @RequestMapping("/getBlogDetail")
  public Blog getBlogDetail(Long id) {
    return blogDao.findOne(id);
  }

  @RequestMapping("/generateGitPageBolg")
  public String generateGitPageBolg() {
    List<Blog> blogs = blogDao.findAll();
    blogs.forEach(blog -> {
      String path = "/project/gitpagesblog/source/_posts/" + blog.getTitle() + ".md";
      File file = new File(path);
      if (!file.exists()) {
        try {
          file.createNewFile();
          logger.info("创建文件{}成功", blog.getTitle());
        } catch (IOException e) {
          logger.error("创建文件{}失败", blog.getTitle());
          return;
        }
      }
      FileWriter fileWriter = null;
      try {
        fileWriter = new FileWriter(file);
        StringBuilder content = new StringBuilder();
        content.append("---\n")
            .append("title: " + blog.getTitle() + "\n")
            .append("date: " + DateFormatUtils.format(blog.getCreateDt(), "yyyy-MM-dd HH:mm:ss") + "\n")
            .append("tags: " + getCategory(blog.getCategory()) + "\n")
            .append("---\n\n").append(blog.getContent());
        fileWriter.write(content.toString());
        fileWriter.close(); // 关闭数据流
      } catch (IOException e) {
        logger.error("写入文件{}失败", blog.getTitle());
        return;
      } finally {
        if (fileWriter != null) {
          try {
            fileWriter.close();
          } catch (IOException e) {
            logger.error("写入文件{},IO流关闭异常", blog.getTitle());
            return;
          }
        }
      }
      logger.info("更新文件{}内容成功", blog.getTitle());
    });
    return "success";
  }

  private String getCategory(Long id) {
    Map<Integer, String> map = ImmutableMap
        .of(1, "jdk", 2, "spring", 3, "生活", 4, "数据结构与算法", 5, "技术周边");
    return map.get((Long) id);
  }

}
