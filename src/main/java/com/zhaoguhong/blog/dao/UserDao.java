package com.zhaoguhong.blog.dao;

import com.zhaoguhong.blog.common.base.BaseRepository;
import com.zhaoguhong.blog.entity.User;

public interface  UserDao extends BaseRepository<User> {

  // @Query("from ImportExcelCell where rowId = ?1 and isDeleted = 0")
//   List<User> findByRowId(Long rowId);
  //
}
