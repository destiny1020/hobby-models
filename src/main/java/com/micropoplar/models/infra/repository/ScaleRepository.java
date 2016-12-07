package com.micropoplar.models.infra.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.micropoplar.models.infra.domain.Scale;

/**
 * 比例DAO。
 * 
 * @author ruixiang
 *
 */
public interface ScaleRepository extends JpaRepository<Scale, Long> {

  /**
   * 根据比例名称获取比例对象。
   * 
   * @param scaleName
   * @return
   */
  Scale findByName(String scaleName);

}
