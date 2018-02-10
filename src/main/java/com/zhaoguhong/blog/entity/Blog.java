package com.zhaoguhong.blog.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.zhaoguhong.blog.common.base.BaseEntity;

/**
 * Blog 实体类
 *
 * @author zhaoguhong
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
  private Long categoryId;

  /**
   * 分类名称
   */
  private String categoryName;

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

  public Long getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(Long categoryId) {
    this.categoryId = categoryId;
  }

  @Transient
  public String getCategoryName() {
    return categoryName;
  }

  public void setCategoryName(String categoryName) {
    this.categoryName = categoryName;
  }

  @Override
  public String toString() {
    return "Blog [id=" + id + ", title=" + title + ", content=" + content + ", categoryId=" + categoryId + "]";
  }

}
