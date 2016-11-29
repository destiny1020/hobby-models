package com.micropoplar.models.crawl.repository;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.micropoplar.models.crawl.domain.OneNNNRecordListRaw;

/**
 * 保存1999列表项目的原始爬取信息。
 * 
 * @author ruixiang
 *
 */
@Transactional
public interface OneNNNRecordListRawRepository extends JpaRepository<OneNNNRecordListRaw, Long> {

  /**
   * 根据分页参数得到待处理的任务。
   * 
   * @param pageRequest
   * @return
   */
  @Query("select r from OneNNNRecordListRaw r where r.hasCrawled = false and r.hasCancelled = false")
  Page<OneNNNRecordListRaw> findByRemainingTask(Pageable pageRequest);

}
