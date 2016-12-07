package com.micropoplar.models.infra;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringRunner;

import com.micropoplar.models.crawl.domain.OneNNNRecordListRaw;
import com.micropoplar.models.crawl.repository.OneNNNRecordListRawRepository;
import com.micropoplar.models.infra.domain.Series;
import com.micropoplar.models.infra.service.SeriesService;

/**
 * 和系列元数据相关的初始化操作。
 * 
 * @author ruixiang
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SeriesTest extends AbstractTransactionalJUnit4SpringContextTests {

  @Autowired
  private OneNNNRecordListRawRepository listRecordRepo;

  @Autowired
  private SeriesService seriesService;

  private Set<String> processedSeries;

  @Before
  public void before() {
    processedSeries = new HashSet<>();
  }

  /**
   * 准备系列元数据
   */
  @Test
  @Commit
  public void fillSeries() {
    List<OneNNNRecordListRaw> items = listRecordRepo.findAll();
    System.out.println("处理列表原始记录条数：" + items.size());
    for (int i = 1; i <= items.size(); i++) {
      System.out.println(String.format("处理中: %d/%d", i, items.size()));
      String[] seriesArray = items.get(i - 1).getSeries().split("、");
      Arrays.asList(seriesArray).forEach(series -> {
        if (!processedSeries.contains(series) && StringUtils.isNotBlank(series)) {
          System.out.println("保存新的系列: " + series);
          seriesService.save(new Series(series));
          processedSeries.add(series);
        }
      });
    }
  }

}
