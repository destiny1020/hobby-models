package com.micropoplar.models.infra.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.micropoplar.models.infra.domain.Scale;
import com.micropoplar.models.infra.repository.ScaleRepository;

/**
 * 比例元数据维护服务。
 * 
 * @author ruixiang
 *
 */
@Service
@Transactional
public class ScaleService {

  @Autowired
  private ScaleRepository scaleRepo;

  /**
   * 保存比例。
   * 
   * @param brand
   * @return
   */
  public Scale save(Scale scale) {
    return scaleRepo.save(scale);
  }

  /**
   * 查看比例是否已经存在。
   * 
   * @param scaleName
   * @return
   */
  public boolean existsByName(String scaleName) {
    Scale scale = scaleRepo.findByName(scaleName);
    return scale != null;
  }

}
