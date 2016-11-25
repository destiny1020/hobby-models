package com.micropoplar.models.crawl;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.micropoplar.models.crawl.repository.OneNNNRecordListRawRepository;

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
  private OneNNNRecordListRawRepository recordListRawRepo;

}
