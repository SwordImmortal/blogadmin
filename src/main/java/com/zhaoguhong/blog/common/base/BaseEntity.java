package com.zhaoguhong.blog.common.base;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.MappedSuperclass;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * entity base 类
 * 
 * @author zhaoguhong
 */
@MappedSuperclass
public abstract class BaseEntity implements Serializable {
  private static final long serialVersionUID = 1L;
  private Date createDt;
  private Date updateDt;

  /**
   * 逻辑删除标识0：正常1
   */
  private Integer isDeleted;

  @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
  public Date getCreateDt() {
    return createDt;
  }

  public void setCreateDt(Date createDt) {
    this.createDt = createDt;
  }

  public Date getUpdateDt() {
    return updateDt;
  }

  public void setUpdateDt(Date updateDt) {
    this.updateDt = updateDt;
  }

  public Integer getIsDeleted() {
    return isDeleted;
  }

  public void setIsDeleted(Integer isDeleted) {
    this.isDeleted = isDeleted;
  }
}
