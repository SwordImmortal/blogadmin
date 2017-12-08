package com.zhaoguhong.blog.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.zhaoguhong.blog.common.base.BaseEntity;

/**
 * 
 *
 * @author
 * @date 2017-09-24
 */
@Entity
public class Blog extends BaseEntity {
  private Long id;
  /**
   * 标题
   */
  private String title;

  /**
   * 内容
   */
  private String content;
  
  /**
   * 分类
   */
  private Long category;
  
  @Id
  @GeneratedValue
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public Long getCategory() {
    return category;
  }

  public void setCategory(Long category) {
    this.category = category;
  }

  @Override
  public String toString() {
    return "Blog [id=" + id + ", title=" + title + ", content=" + content + ", category=" + category + "]";
  }

}
