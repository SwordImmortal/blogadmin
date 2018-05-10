package com.zhaoguhong.blog.core.cache;

import java.util.concurrent.Callable;

public interface CacheService {

  /**
   * 根据key查询缓存
   * 
   * @param key
   * @return
   */
  <T> T get(String key);

  /**
   * 根据key删除缓存
   * 
   * @param key
   * @return
   */
  void remove(String key);

  /**
   * 根据key查询缓存,如果查询不到，返回回调的值，并把该值放入缓存
   * 
   * @param key
   * @param valueLoader
   * @return
   */
  <T> T get(String key, Callable<T> valueLoader);

}
