package com.micropoplar.models.infra.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.micropoplar.models.infra.domain.Series;

/**
 * 系列DAO。
 * 
 * @author ruixiang
 *
 */
public interface SeriesRepository extends JpaRepository<Series, Long> {

  /**
   * 根据系列名称获取系列对象。
   * 
   * @param scaleName
   * @return
   */
  Series findByName(String seriesName);

}
