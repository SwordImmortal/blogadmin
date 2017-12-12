package com.zhaoguhong.blog.common.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity> extends JpaRepository<T, Long> {

  void saveEntity(T entity);

  void updateEntity(T entity);

  void deteleEntity(T entity);

}
