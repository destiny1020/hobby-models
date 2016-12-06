package com.micropoplar.models.crawl;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.transaction.TestTransaction;

import com.micropoplar.models.crawl.biz.OneNNNCrawlListPage;
import com.micropoplar.models.crawl.domain.OneNNNCrawlTarget;
import com.micropoplar.models.crawl.domain.OneNNNRecordListRaw;
import com.micropoplar.models.crawl.repository.OneNNNCrawlTargetRepository;
import com.micropoplar.models.crawl.service.OneNNNCrawlListService;
import com.micropoplar.models.infra.domain.Brand;
import com.micropoplar.models.infra.service.BrandService;

/**
 * 列表抓取服务行为测试。
 * 
 * @author ruixiang
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class OneNNNCrawlListTest extends AbstractTransactionalJUnit4SpringContextTests {

  @Autowired
  private OneNNNCrawlListService recordListRawService;

  @Autowired
  private BrandService brandService;

  @Autowired
  private OneNNNCrawlTargetRepository crawlTargetRepo;

  @Test
  @Commit
  public void testSaveListItems() {
    List<OneNNNCrawlTarget> targets = crawlTargetRepo.findAll();
    targets.forEach(target -> {
      OneNNNCrawlListPage pageCrawler = new OneNNNCrawlListPage(String.valueOf(target.getSn()));
      System.out.println(String.format("开始爬取类型: %s - %s - %s", target.getType(), target.getName(),
          target.getScale()));

      final AtomicInteger totalChanged = new AtomicInteger(0);

      do {
        pageCrawler = pageCrawler.navToNext();
        try {
          pageCrawler.connect();
        } catch (Exception e) {
          e.printStackTrace();
          return;
        }

        if (TestTransaction.isActive()) {
          TestTransaction.end();
        }
        TestTransaction.start();

        List<OneNNNRecordListRaw> items = pageCrawler.parseItems();
        items.forEach(item -> {
          if (!recordListRawService.isTheSameWithLatestRawListItem(item)) {
            System.out.println("保存新的爬取记录: " + item.getSn());

            // 判断是否需要保存品牌记录
            String[] makers = item.getMakers().split("、");
            Arrays.asList(makers).forEach(maker -> {
              if (!brandService.existsByNameJapanese(maker)) {
                System.out.println("保存新的品牌: " + maker);
                brandService.save(new Brand(maker));
              }
            });

            totalChanged.incrementAndGet();
            recordListRawService.saveRecord(item);
          } else {
            System.out.println("和现有的爬取记录相同，不需要保存: " + item.getSn());
          }
        });

        TestTransaction.flagForCommit();
        TestTransaction.end();
      } while (pageCrawler.hasNextPage());

      System.out.println(String.format("结束爬取类型: %s - %s - %s, 变更数量: %d", target.getType(),
          target.getName(), target.getScale(), totalChanged.get()));
    });
  }

}
