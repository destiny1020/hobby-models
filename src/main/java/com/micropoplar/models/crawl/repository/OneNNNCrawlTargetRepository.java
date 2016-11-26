package com.micropoplar.models.crawl.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.micropoplar.models.crawl.domain.OneNNNCrawlTarget;

/**
 * 1999爬取目标信息DAO。
 * 
 * @author ruixiang
 *
 */
public interface OneNNNCrawlTargetRepository extends JpaRepository<OneNNNCrawlTarget, Long> {

}
