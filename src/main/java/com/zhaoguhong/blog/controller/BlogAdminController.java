package com.zhaoguhong.blog.controller;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Objects;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.zhaoguhong.blog.core.ContextHolder;
import com.zhaoguhong.blog.core.dao.BaseDao;
import com.zhaoguhong.blog.dao.BlogDao;
import com.zhaoguhong.blog.dao.CategoryDao;
import com.zhaoguhong.blog.entity.Blog;
import com.zhaoguhong.blog.entity.Category;
import com.zhaoguhong.blog.util.Common;
import com.zhaoguhong.blog.util.HttpUtil;
import com.zhaoguhong.blog.util.RegularUtil;

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
  private BaseDao baseDao;
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
  public Map<String, Object> updateBlog(@RequestBody Map<String, Object> map) {
    Map<String, Object> result = Maps.newHashMap();
    result.put("status", true);
    Long id = MapUtils.getLong(map, "id");
    Integer isDeleted = MapUtils.getInteger(map, "isDeleted");
    if (Objects.equal(isDeleted, 1)) {
      String ids = MapUtils.getString(map, "ids");
      if (StringUtils.isNotBlank(ids)) {
        Common.spiltStrToLongList(ids).forEach(delId -> blogDao.deteleEntity(blogDao.getOne(delId)));
      }
      return result;
    }
    String title = MapUtils.getString(map, "title");
    Long category = MapUtils.getLong((Map) MapUtils.getObject(map, "category"), "selected");
    String content = MapUtils.getString(map, "content");
    String tag = MapUtils.getString(map, "tag");
    if (StringUtils.isAnyBlank(title, content, tag)) {
      result.put("status", false);
      result.put("info", "有必填项为空！");
      return result;
    }
    Blog blog = (id == null ? new Blog() : blogDao.getOne(id));
    blog.setTitle(title);
    blog.setContent(content);
    blog.setCategoryId(category);
    blog.setTag(tag);
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
    List<Blog> blogs = baseDao.findAll(Blog.class);
    for (Blog blog : blogs) {
      blog.setCategoryName(getCategory(blog.getCategoryId()));
    }
    System.out.println(ContextHolder.getResponse());
    return blogs;
  }

  @RequestMapping("/getBlogDetail")
  public Blog getBlogDetail(Long id) {
    return blogDao.findOne(id);
  }

  @RequestMapping("/generateBlog")
  public String generateGitPageBolg() {
    String blogPath = "/project/gitpagesblog/source/_posts/";
    List<Blog> blogs = baseDao.findAll(Blog.class);
    Set<String> names = Sets.newHashSet();
    for (Blog blog : blogs) {
      names.add(blog.getTitle());
    }
    File rootDirectory = new File(blogPath);
    File[] files = rootDirectory.listFiles();
    // 删除原来多余的文件或者文件夹
    for (File file : files) {
      if (file.isDirectory()) {
        if (!names.contains(StringUtils.substringBefore(file.getName(), "."))) {
          try {
            FileUtils.forceDelete(file);
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
          logger.error("删除文件{}", file.getName());
        }
      }
    }
    for (Blog blog : blogs) {
      String path = blogPath + blog.getTitle() + ".md";
      File file = new File(path);
      String contentStr = blog.getContent();
      try {
        // ![](http://images2017.cnblogs.com/blog/1161430/201802/1161430-20180210190110170-907437034.png)
        List<String> images = RegularUtil.find(contentStr, "!\\[.*\\]\\((http.*?)\\)");
        for (String imgUrl : images) {
          try {
            File imgFileDir = new File(blogPath + blog.getTitle());
            if (!imgFileDir.exists()) {
              imgFileDir.mkdir();
              logger.info("创建文件夹：{}", imgFileDir.getName());
            }
            String imgName = StringUtils.substringAfterLast(imgUrl, "/");
            File imgFile = new File(imgFileDir.getPath() + "/" + imgName);
            if (!imgFile.exists()) {
              byte[] byteArray = HttpUtil.getByteArray(imgUrl, null);
              FileUtils.writeByteArrayToFile(imgFile, byteArray);
              logger.info("爬取图片：{}", imgName);
            }
            contentStr = contentStr.replace(imgUrl, blog.getTitle() + "/" + imgName);
          } catch (Exception e) {
            throw new RuntimeException(e);
          }
        }
        StringBuilder content = new StringBuilder();
        content.append("---\n")
            .append("title: " + blog.getTitle() + "\n")
            .append("date: " + DateFormatUtils.format(blog.getCreateDt(), "yyyy-MM-dd HH:mm:ss") + "\n")
            .append("categories: " + getCategory(blog.getCategoryId()) + "\n");
        if (StringUtils.isNotBlank(blog.getTag())) {
          content.append("tags: [" + blog.getTag() + "]\n");
        }
        content.append("---\n\n").append(contentStr);
        FileUtils.writeStringToFile(file, content.toString(), Charset.forName("utf-8"));
      } catch (IOException e) {
        logger.error("写入文件{}失败", blog.getTitle());
      }
    }
    return "success";
  }

  public String getCategory(Long id) {
    if (categorys.isEmpty()) {
      List<Category> categoryList = baseDao.findAll(Category.class);
      categoryList.forEach(category -> categorys.put(category.getId(), category.getName()));
    }
    return categorys.get(id);
  }

}
