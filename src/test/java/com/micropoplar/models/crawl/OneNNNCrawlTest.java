package com.micropoplar.models.crawl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.transaction.TestTransaction;

import com.micropoplar.models.crawl.domain.OneNNNRecordListRaw;
import com.micropoplar.models.crawl.domain.OneNNNRecordRaw;
import com.micropoplar.models.crawl.repository.OneNNNRawRecordRepository;
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
  private EntityManager em;

  @Autowired
  private OneNNNCrawlService crawlService;

  @Autowired
  private OneNNNRecordListRawRepository rawListRecordRepo;

  @Autowired
  private OneNNNRawRecordRepository rawRecordRepo;

  @Test
  public void testCrawlSingleItem() throws MalformedURLException, IOException {
    String sn = "10006724";

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
   * 删除爬取详情表中的损坏记录。
   */
  @Test
  @Transactional
  @Rollback(false)
  public void testDeleteCorruptedRecord() {
    // String sn = "10434320";
    //
    // OneNNNRecordRaw recordRaw = rawRecordRepo.findBySn(sn);
    // rawRecordRepo.delete(recordRaw);

    rawRecordRepo.delete(1130L);
  }

  /**
   * 爬取详情页。
   */
  @Test
  @Commit
  public void testCrawlItems() {
    int size = 10;

    Page<OneNNNRecordListRaw> tasks = null;
    int page = 1;
    do {
      System.out.println("获取下一页的数据: " + page++);

      if (TestTransaction.isActive()) {
        TestTransaction.end();
      }
      TestTransaction.start();

      // 每次获取10条数据
      tasks = rawListRecordRepo.findByRemainingTask(new PageRequest(0, size));
      List<OneNNNRecordRaw> crawledRecords = tasks.getContent().parallelStream().map(task -> {
        OneNNNRecordRaw crawledRecord = null;
        try {
          crawledRecord = crawlService.crawl(task.getSn());
          if (StringUtils.isNotBlank(crawledRecord.getCoverUrl())) {
            task.setHasCrawled(Boolean.TRUE);
          } else {
            task.setHasCancelled(Boolean.TRUE);
          }
        } catch (Exception e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
          task.setHasCrawled(Boolean.FALSE);
          task.setHasCancelled(Boolean.FALSE);
        }
        return crawledRecord;
      }).filter(crawledRecord -> {
        return crawledRecord != null && crawledRecord.getShouldSave().booleanValue();
      }).collect(Collectors.toList());

      rawRecordRepo.save(crawledRecords);
      rawListRecordRepo.save(tasks);
      TestTransaction.flagForCommit();
      TestTransaction.end();
    } while (tasks != null && tasks.getContent() != null && tasks.getContent().size() > 0);

  }

}
