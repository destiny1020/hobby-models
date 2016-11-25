package com.micropoplar.models.crawl.util;

import com.micropoplar.models.crawl.constant.CrawlContant;

/**
 * 1999抓取服务相关帮助方法。
 * 
 * @author ruixiang
 *
 */
public class OneNNNCrawlerUtil {

  /**
   * 添加站点URL作为前缀。
   * 
   * @param imageHref
   * @return
   */
  public static String addPrefix(String imageHref) {
    if (imageHref.startsWith("/")) {
      imageHref = CrawlContant.CRAWL_1999_SITE_BASE + imageHref;
    }

    return imageHref;
  }

}
