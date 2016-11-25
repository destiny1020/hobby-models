package com.micropoplar.models.crawl.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.micropoplar.models.crawl.domain.OneNNNRecordListRaw;

/**
 * 保存1999列表项目的原始爬取信息。
 * 
 * @author ruixiang
 *
 */
public interface OneNNNRecordListRawRepository extends JpaRepository<OneNNNRecordListRaw, Long> {

}
