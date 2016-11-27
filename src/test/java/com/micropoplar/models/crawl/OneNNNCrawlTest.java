package com.micropoplar.models.crawl;

import java.io.IOException;
import java.net.MalformedURLException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.transaction.TestTransaction;

import com.micropoplar.models.crawl.domain.OneNNNRecordListRaw;
import com.micropoplar.models.crawl.repository.OneNNNRecordListRawRepository;
import com.micropoplar.models.crawl.service.OneNNNCrawlService;

/**
 * 测试抓取1999商品页面。
 * 
 * @author ruixiang
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class OneNNNCrawlTest extends AbstractTransactionalJUnit4SpringContextTests {

  @Autowired
  private OneNNNCrawlService crawlService;

  @Autowired
  private OneNNNRecordListRawRepository rawListRecordRepo;

  @Test
  public void testCrawlSingleItem() throws MalformedURLException, IOException {
    String sn = "10411171";

    crawlService.crawl(sn);

    TestTransaction.flagForCommit();
    TestTransaction.end();

    // 开启新的TX
    TestTransaction.start();
    sn = "10434320";

    crawlService.crawl(sn);

    TestTransaction.flagForCommit();
    TestTransaction.end();
  }

  /**
   * 爬取详情页。
   */
  @Test
  public void testCrawlItems() {
    int size = 50;

    // TODO: 重构
    Page<OneNNNRecordListRaw> tasks =
        rawListRecordRepo.findByRemainingTask(new PageRequest(0, size));
    int totalPages = tasks.getTotalPages();

    for (int page = 0; page < totalPages; page++) {
      tasks = rawListRecordRepo.findByRemainingTask(new PageRequest(page, size));
      tasks.getContent().parallelStream().forEach(task -> {
        try {
          crawlService.crawl(task.getSn());
          task.setHasCrawled(Boolean.TRUE);
          rawListRecordRepo.save(task);
        } catch (Exception e) {
          e.printStackTrace();
        }
      });

      TestTransaction.flagForCommit();
      TestTransaction.end();
      TestTransaction.start();
    }

  }

}
