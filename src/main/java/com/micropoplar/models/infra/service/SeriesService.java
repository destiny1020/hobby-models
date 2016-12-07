package com.micropoplar.models.infra.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.micropoplar.models.infra.domain.Series;
import com.micropoplar.models.infra.repository.SeriesRepository;

/**
 * 系列元数据维护服务。
 * 
 * @author ruixiang
 *
 */
@Service
@Transactional
public class SeriesService {

  @Autowired
  private SeriesRepository seriesRepo;

  /**
   * 保存系列。
   * 
   * @param series
   * @return
   */
  public Series save(Series series) {
    return seriesRepo.save(series);
  }

  /**
   * 查看系列是否已经存在。
   * 
   * @param seriesName
   * @return
   */
  public boolean existsByName(String seriesName) {
    Series series = seriesRepo.findByName(seriesName);
    return series != null;
  }

}
