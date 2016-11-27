package com.micropoplar.models.crawl.util;

import java.io.IOException;
import java.util.Random;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.micropoplar.models.crawl.constant.CrawlUserAgentConstant;

/**
 * UA相关帮助方法。
 * 
 * @author ruixiang
 *
 */
public class CrawlConnectionUtil {

  private static final Random RAND = new Random();

  private static final int RETRY_MAX = 5;

  private static final Logger LOGGER = LoggerFactory.getLogger(CrawlConnectionUtil.class);

  /**
   * 根据URL获取HTML。
   * 
   * @param url
   * @return
   * @throws IOException
   */
  public static Document getDocument(String url) throws IOException {
    Document document = null;
    int retryTimes = 0;
    while (retryTimes < RETRY_MAX) {
      try {
        LOGGER.debug(String.format("[爬虫 - HTML] 第%d次尝试获取HTML: %s", retryTimes + 1, url));
        document = Jsoup.connect(url).userAgent(randomUserAgent()).referrer(randomReferrer())
            .timeout(20000).get();
        return document;
      } catch (IOException ioe) {
        retryTimes++;
        if (retryTimes >= RETRY_MAX) {
          throw new RuntimeException(String.format("获取URL失败: %s", url));
        }
      }
    }

    return document;
  }

  /**
   * 获取随机的UA。
   * 
   * @return
   */
  private static String randomUserAgent() {
    return CrawlUserAgentConstant.USER_AGENTS[RAND
        .nextInt(CrawlUserAgentConstant.USER_AGENTS.length)];
  }

  /**
   * 获取随机的Referrer。
   * 
   * @return
   */
  private static String randomReferrer() {
    return CrawlUserAgentConstant.REFERERS[RAND.nextInt(CrawlUserAgentConstant.REFERERS.length)];
  }

}
