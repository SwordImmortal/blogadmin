package com.zhaoguhong.blog.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.zhaoguhong.blog.common.base.BaseEntity;

import lombok.Builder;
import lombok.Data;

/**
 * 分类
 * 
 * @author zhaoguhong
 * @date 2017年12月12日
 */
@Entity
@Data
@Builder
public class Category extends BaseEntity {
  
  private Long id;

  /**
   * 类别名称
   */
  private String name;

  @Id
  @GeneratedValue
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Override
  public String toString() {
    return "Category [id=" + id + ", name=" + name + "]";
  }
  

}
