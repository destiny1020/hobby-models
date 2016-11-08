package com.micropoplar.models.crawl;

import java.io.IOException;
import java.net.MalformedURLException;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import com.micropoplar.models.crawl.service.OneNNNCrawlService;

/**
 * 测试抓取1999商品页面。
 * 
 * @author ruixiang
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class OneNNNCrawlTest {

  @Autowired
  private OneNNNCrawlService crawlService;

  @Test
  @Transactional
  @Rollback(false)
  public void testCrawlSingleItem() throws MalformedURLException, IOException {
    // 中国驱逐舰
    // String sn = "10424885";

    String sn = "10376808";

    crawlService.crawl(sn);
  }

}
