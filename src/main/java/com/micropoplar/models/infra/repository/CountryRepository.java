package com.micropoplar.models.infra.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.micropoplar.models.infra.domain.Country;

/**
 * 国家DAO。
 * 
 * @author ruixiang
 *
 */
public interface CountryRepository extends JpaRepository<Country, Long> {

  /**
   * 查看国家日文名是否存在。
   * 
   * @param nameJapanese
   * @return
   */
  int countByNameJapanese(String nameJapanese);

}
