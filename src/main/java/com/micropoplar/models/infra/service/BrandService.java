package com.micropoplar.models.infra.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.micropoplar.models.infra.domain.Brand;
import com.micropoplar.models.infra.repository.BrandRepository;

/**
 * 品牌相关服务。
 * 
 * @author ruixiang
 *
 */
@Service
public class BrandService {

  @Autowired
  private BrandRepository brandRepo;

  /**
   * 保存品牌。
   * 
   * @param brand
   * @return
   */
  public Brand save(Brand brand) {
    return brandRepo.save(brand);
  }

  /**
   * 查看品牌日文名是否存在。
   * 
   * @param nameJapanese
   * @return
   */
  public boolean existsByNameJapanese(String nameJapanese) {
    return brandRepo.countByNameJapanese(nameJapanese) > 0;
  }

}
