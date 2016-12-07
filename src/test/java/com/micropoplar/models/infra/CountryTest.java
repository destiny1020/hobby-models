package com.micropoplar.models.infra;

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

import com.micropoplar.models.crawl.domain.OneNNNRecordRaw;
import com.micropoplar.models.crawl.repository.OneNNNRawRecordRepository;
import com.micropoplar.models.infra.domain.Country;
import com.micropoplar.models.infra.repository.CountryRepository;

/**
 * 和国家元数据相关的初始化操作。
 * 
 * @author ruixiang
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CountryTest extends AbstractTransactionalJUnit4SpringContextTests {

  @Autowired
  private OneNNNRawRecordRepository recordRepo;

  @Autowired
  private CountryRepository countryRepo;

  private Set<String> processedCountries;

  @Before
  public void before() {
    processedCountries = new HashSet<>();
  }

  /**
   * 准备国家元数据
   */
  @Test
  @Commit
  public void fillCountries() {
    List<OneNNNRecordRaw> items = recordRepo.findAll();
    System.out.println("处理列表原始记录条数：" + items.size());
    for (int i = 1; i <= items.size(); i++) {
      System.out.println(String.format("处理中: %d/%d", i, items.size()));
      List<String> countries = items.get(i - 1).getCountries();
      countries.forEach(country -> {
        if (!processedCountries.contains(country) && StringUtils.isNotBlank(country)) {
          System.out.println("保存新的国家: " + country);
          countryRepo.save(new Country(country));
          processedCountries.add(country);
        }
      });
    }
  }

}
