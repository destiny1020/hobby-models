package com.micropoplar.models.crawl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import com.micropoplar.models.crawl.biz.OneNNNCrawlListPage;
import com.micropoplar.models.crawl.domain.OneNNNRecordListRaw;
import com.micropoplar.models.crawl.service.OneNNNCrawlListService;

/**
 * 列表抓取服务行为测试。
 * 
 * @author ruixiang
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class OneNNNCrawlListTest {

  @Autowired
  private OneNNNCrawlListService recordListRawService;

  @Test
  @Transactional
  @Rollback(false)
  public void testSaveTamiyaPages() throws MalformedURLException, IOException {
    // OneNNNCrawlListPage pageCrawler = new OneNNNCrawlListPage("タミヤ");
    OneNNNCrawlListPage pageCrawler = new OneNNNCrawlListPage("ハセガワ");
    do {
      pageCrawler = pageCrawler.navToNext();
      pageCrawler.connect();
      List<OneNNNRecordListRaw> items = pageCrawler.parseItems();
      items.forEach(item -> {
        if (!recordListRawService.isTheSameWithLatestRawListItem(item)) {
          System.out.println("保存新的爬取记录: " + item.getSn());
          recordListRawService.saveRecord(item);
        } else {
          System.out.println("和现有的爬取记录相同，不需要保存: " + item.getSn());
        }
      });
    } while (pageCrawler.hasNextPage());
  }

}
