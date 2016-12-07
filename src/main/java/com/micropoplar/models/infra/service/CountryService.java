package com.micropoplar.models.infra.service;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.micropoplar.models.infra.domain.Country;
import com.micropoplar.models.infra.repository.CountryRepository;

/**
 * 国家元数据相关服务。
 * 
 * @author ruixiang
 *
 */
@Service
@Transactional
public class CountryService {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private CountryRepository countryRepo;

  /**
   * 保存国家。
   * 
   * @param brand
   * @return
   */
  public Country save(Country country) {
    return countryRepo.save(country);
  }

  /**
   * 查看国家日文名是否存在。
   * 
   * @param nameJapanese
   * @return
   */
  public boolean existsByNameJapanese(String nameJapanese) {
    return countryRepo.countByNameJapanese(nameJapanese) > 0;
  }

}
