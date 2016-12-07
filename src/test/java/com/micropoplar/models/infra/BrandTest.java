package com.micropoplar.models.infra;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringRunner;

import com.micropoplar.models.infra.domain.Brand;
import com.micropoplar.models.infra.repository.BrandRepository;
import com.micropoplar.models.infra.service.BrandService;

/**
 * 和品牌元数据相关的初始化操作。
 * 
 * @author ruixiang
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class BrandTest extends AbstractTransactionalJUnit4SpringContextTests {

  @Autowired
  private BrandRepository brandRepo;

  @Autowired
  private BrandService brandService;

  /**
   * 补全缺失code字段的brands数据。
   */
  @Test
  @Commit
  public void fillBrandCode() {
    List<Brand> brands = brandRepo.findAll();

    brands.forEach(brand -> {
      if (StringUtils.isBlank(brand.getCode())) {
        brand.setCode(brandService.getAvailableCode());
        brandService.save(brand);
      }
    });
  }

}
