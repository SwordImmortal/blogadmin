package com.zhaoguhong.util;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections.MapUtils;
import org.apache.http.client.ClientProtocolException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.zhaoguhong.blog.util.HttpUtil;
import com.zhaoguhong.blog.util.RegularUtil;

public class BookSearchTest {
  public static Logger logger = (Logger) LoggerFactory.getLogger(BookSearchTest.class);

  /**
   * 模拟上海图书馆搜索
   */
  @Test
  public void testBookSearch() throws ClientProtocolException, IOException, InterruptedException {
    long startTime = System.currentTimeMillis();
    List<Map<String, Object>> books = Collections.synchronizedList(Lists.newArrayList());
    Map<String, Object> params = Maps.newHashMap();
    params.put("url", "http://opac.shl.findplus.cn/?page=1&pagesize=1000&h=search_list&query=TI:");
    params.put("bookName", "三体");
    String url = MapUtils.getString(params, "url");
    String bookName = MapUtils.getString(params, "bookName");
    bookName = "%22" + bookName + "%22";// 精确匹配
    bookName += "&action[addexpander][]=fulltext";
    String content = HttpUtil.getString(url + bookName);
    // 正则 ^< 排除<
    List<String> bookUrls = RegularUtil.find(content, "<a href=\"([^<]*)\" target='_blank'>详细信息</a>");
    System.out.println("爬取首页完毕，共" + bookUrls.size() + "本书");
    // 核心线程池大小 线程池最大容量大小 线程池空闲时，线程存活的时间 时间单位 任务队列
    ThreadPoolExecutor executor =
        new ThreadPoolExecutor(20, 30, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
    Set<String> hasExecuteUrls = Collections.synchronizedSet(Sets.newHashSet());// 已经执行过的url
    Set<String> noExecuteUrls = Collections.synchronizedSet(Sets.newHashSet());// 还未执行过的url
    bookUrls.forEach(detailUrl -> {
      executor.execute(new BookExecutor(books, detailUrl, hasExecuteUrls, noExecuteUrls));
    });
    logger.info("首页抓取完毕");
    while (true) {
      // 需要加锁
      for (Iterator<String> iterator = noExecuteUrls.iterator(); iterator.hasNext();) {
        String noUrl = iterator.next();
        executor.execute(new BookExecutor(books, noUrl, hasExecuteUrls, noExecuteUrls));
        hasExecuteUrls.add(noUrl);
        iterator.remove();
        logger.info("放入线程:" + noUrl.substring(noUrl.lastIndexOf("Farticles_id")));
      }
      if (noExecuteUrls.isEmpty() && executor.getActiveCount() == 0) {
        logger.info("所有的子线程都结束了！");
        break;
      }
      Thread.sleep(2000);
    }
    executor.shutdown();
    logger.info("全部放入线程");
    long endTime = System.currentTimeMillis();
    logger.info(books.toString());
    logger.info("一共耗时：" + (endTime - startTime) / 1000 + "秒");
  }
}


class BookExecutor implements Runnable {
  String detailUrl;
  List<Map<String, Object>> books;
  Set<String> hasExecuteUrls;// 已经执行过的url
  Set<String> noExecuteUrls;// 还未执行过的url

  public BookExecutor(List<Map<String, Object>> books, String detailUrl, Set<String> hasExecuteUrls, Set<String> noExecuteUrls) {
    this.detailUrl = detailUrl;
    this.books = books;
    this.hasExecuteUrls = hasExecuteUrls;
    this.noExecuteUrls = noExecuteUrls;
  }

  @Override
  public void run() {
    String detailsContent = HttpUtil.getString("http://opac.shl.findplus.cn/" + detailUrl);
    // 获取分页的html
    List<String> pages =
        RegularUtil.find(detailsContent, "<div class=\"page_div\" style=\"text-align:right;\">([\\s\\S]*?)</div>");
    if (!pages.isEmpty()) {
      String page = pages.get(0);
      // 获取分页中当前页后面的html
      page = page.substring(page.indexOf("</strong>"));
      List<String> allUrls = RegularUtil.find(page, "<a href=\"/([\\s\\S]*?)\">");
      allUrls.removeAll(hasExecuteUrls);
      noExecuteUrls.addAll(allUrls);
    }
    List<String> details = RegularUtil.find(detailsContent, "<tr class=\"listgcd\"([\\s\\S]*?)</tr>");
    details.forEach(detail -> {
      List<String> detailBooks = RegularUtil.find(detail, "<td>([\\s\\S]*?)</td>");
      if ("浦东新区分馆".equals(detailBooks.get(0)) && "归还".equals(detailBooks.get(3))) {
        Map<String, Object> book = Maps.newHashMap();
        book.put("library", detailBooks.get(0));
        book.put("space", detailBooks.get(1));
        book.put("number", detailBooks.get(2));// 索书号
        book.put("status", detailBooks.get(3));
        if (detailBooks.size() == 6) {
          book.put("bookNumber", detailBooks.get(5));
        } else {
          book.put("bookNumber", detailBooks.get(6));
        }
        books.add(book);
      }
    });
    System.out.println("url:" + detailUrl.substring(detailUrl.lastIndexOf("Farticles_id")) + "执行完毕");
  }

}
