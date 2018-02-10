package com.zhaoguhong.blog.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Objects;
import com.google.common.collect.Maps;
import com.zhaoguhong.blog.core.dao.HibernateDao;
import com.zhaoguhong.blog.dao.BlogDao;
import com.zhaoguhong.blog.dao.CategoryDao;
import com.zhaoguhong.blog.entity.Blog;
import com.zhaoguhong.blog.entity.Category;
import com.zhaoguhong.blog.util.Common;

/**
 * 博客管理 Controller
 * 
 * @author zhaoguhong
 * @date 2017年12月15日
 */
@RestController
@RequestMapping("/admin")
public class BlogAdminController {
  @Resource
  private BlogDao blogDao;
  @Resource
  private HibernateDao hiDao;
  @Resource
  private CategoryDao categoryDao;
  private Logger logger = LoggerFactory.getLogger(getClass());
  private Map<Long, String> categorys = Collections.synchronizedMap(Maps.newHashMap());

  @RequestMapping("/test")
  public String index() {
    System.out.print("test");
    return "index";
  }

  @RequestMapping("/updateBlog")
  @Transactional
  public Map<String, Object> updateBlog(@RequestParam Map<String, Object> map) {
    Map<String, Object> result = Maps.newHashMap();
    result.put("status", true);
    Long id = MapUtils.getLong(map, "id");
    String title = MapUtils.getString(map, "title");
    Long category = MapUtils.getLong(map, "category");
    String content = MapUtils.getString(map, "content");
    Integer isDeleted = MapUtils.getInteger(map, "isDeleted");
    if (Objects.equal(isDeleted, 1)) {
      String ids = MapUtils.getString(map, "ids");
      if (StringUtils.isNotBlank(ids)) {
        Common.spiltStrToLongList(ids).forEach(delId -> blogDao.deteleEntity(blogDao.getOne(delId)));
      }
      return result;
    }
    if (StringUtils.isAnyBlank(title, content)) {
      result.put("status", false);
      result.put("info", "有必填项为空！");
      return result;
    }
    Blog blog = (id == null ? new Blog() : blogDao.getOne(id));
    blog.setTitle(title);
    blog.setContent(content);
    blog.setCategoryId(category);
    if (id == null) {
      blogDao.saveEntity(blog);
    } else {
      blogDao.updateEntity(blog);
    }
    result.put("id", blog.getId());
    return result;
  }

  @RequestMapping("/getBlogs")
  public List<Blog> getBlogs(@RequestParam Map<String, Object> map) {
    List<Blog> blogs = hiDao.findAll(Blog.class);
    for (Blog blog : blogs) {
      blog.setCategoryName(getCategory(blog.getCategoryId()));
      if (blog.getContent() != null && blog.getContent().trim().length() > 20) {
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
    List<Blog> blogs = hiDao.findAll(Blog.class);
    File rootDirectory = new File("/project/gitpagesblog/source/_posts/");
    File[] files = rootDirectory.listFiles();
    for (File file : files) {
      file.delete();
      logger.error("删除文件{}", file.getName());
    }
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
            .append("tags: " + getCategory(blog.getCategoryId()) + "\n")
            .append("---\n\n").append(blog.getContent());
        fileWriter.write(content.toString());
      } catch (IOException e) {
        logger.error("写入文件{}失败", blog.getTitle());
        return;
      } finally {
        IOUtils.closeQuietly(fileWriter);// 关闭数据流
      }
    });
    return "success";
  }

  public String getCategory(Long id) {
    if (categorys.isEmpty()) {
      List<Category> categoryList = hiDao.findAll(Category.class);
      categoryList.forEach(category -> categorys.put(category.getId(), category.getName()));
    }
    return categorys.get(id);
  }

}
