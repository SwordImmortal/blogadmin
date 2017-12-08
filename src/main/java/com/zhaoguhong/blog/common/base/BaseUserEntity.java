package com.zhaoguhong.blog.common.base;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * entity base 类
 * @author zhaoguhong
 */
@MappedSuperclass
public abstract class BaseUserEntity extends BaseEntity{
  private Long createBy;
  private Long updateBy;

  @Column(name = "CREATE_BY", updatable = false)
  public Long getCreateBy() {
    return createBy;
  }

  public void setCreateBy(Long createBy) {
    this.createBy = createBy;
  }

  @Column(name = "UPDATE_BY", insertable = false)
  public Long getUpdateBy() {
    return updateBy;
  }

  public void setUpdateBy(Long updateBy) {
    this.updateBy = updateBy;
  }
}
