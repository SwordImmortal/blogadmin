package com.zhaoguhong.blog.core.cache;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;

public class CacheServiceImpl implements CacheService {
  RemovalListener<String, String> listener = new RemovalListener<String, String>() {
    // 删除监听器
    public void onRemoval(RemovalNotification<String, String> notification) {
      System.out.println("key:" + notification.getKey() + "value:" + notification.getValue() + "删除");
    }
  };

  // Cache 还有一个子接口LoadingCache，LoadingCache在缓存项不存在时可以自动加载缓存
  final static Cache<String, Object> cache = CacheBuilder.newBuilder()
      .maximumSize(10000)// 设置缓存最大容量
      // 设置cache的初始大小为10，要合理设置该值
      .initialCapacity(10)
      // 设置并发数为5，即同一时间最多只能有5个线程往cache执行写入操作
      .concurrencyLevel(5)
      // 设置cache中的的对象被写入到缓存后多久过期
      // .expireAfterWrite(10, TimeUnit.SECONDS)
      // 设置cache中的的对象多久没有被访问后过期。
      // .expireAfterAccess(3, TimeUnit.SECONDS)
      // 使用弱引用存储键，没有其他强引用指向key时，自动清除
      // .weakKeys()
      // 使用弱引用存储值，没有其他强引用指向value时，自动清除
      // .weakValues()
      // 开启统计信息开关
      // .recordStats()
      // 构建cache实例
      .build();


  public static void main(String[] args) throws ExecutionException {
    cache.put("name", "zhao");
    cache.invalidate("name");
    System.out.print(cache.getIfPresent("name"));
  }

  @Override
  public <T> T get(String key) {
    return (T) cache.getIfPresent(key);
  }

  @Override
  public <T> T get(String key, Callable<T> valueLoader) {
    try {
      return (T) cache.get(key, valueLoader);
    } catch (ExecutionException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void remove(String key) {
    cache.invalidate(key);
  }

}
