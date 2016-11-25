package com.micropoplar.models.crawl.util;

import java.io.IOException;
import java.util.Random;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.micropoplar.models.crawl.constant.CrawlUserAgentConstant;

/**
 * UA相关帮助方法。
 * 
 * @author ruixiang
 *
 */
public class CrawlConnectionUtil {

  private static final Random RAND = new Random();

  /**
   * 根据URL获取HTML。
   * 
   * @param url
   * @return
   * @throws IOException
   */
  public static Document getDocument(String url) throws IOException {
    return Jsoup.connect(url).userAgent(randomUserAgent()).referrer(randomReferrer()).timeout(20000)
        .get();
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
