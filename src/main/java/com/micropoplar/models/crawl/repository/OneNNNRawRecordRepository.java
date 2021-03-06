package com.micropoplar.models.crawl.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.micropoplar.models.crawl.domain.OneNNNRecordRaw;

/**
 * 保存1999的原始爬取信息。
 * 
 * @author ruixiang
 *
 */
public interface OneNNNRawRecordRepository extends JpaRepository<OneNNNRecordRaw, Long> {

  /**
   * 根据SN获取抓取记录。
   * 
   * @param sn
   * @return
   */
  OneNNNRecordRaw findBySn(String sn);

}
