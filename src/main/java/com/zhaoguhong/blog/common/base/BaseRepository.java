package com.zhaoguhong.blog.common.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface  BaseRepository<T> extends JpaRepository<T,Long> {

   void saveEntity(BaseEntity entity);
  
}
