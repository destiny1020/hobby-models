package com.micropoplar.models.infra.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.micropoplar.models.infra.domain.Brand;

/**
 * 品牌DAO。
 * 
 * @author ruixiang
 *
 */
public interface BrandRepository extends JpaRepository<Brand, Long> {

  /**
   * 查看品牌日文名是否存在。
   * 
   * @param nameJapanese
   * @return
   */
  int countByNameJapanese(String nameJapanese);

}
