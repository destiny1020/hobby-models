package com.micropoplar.models.crawl.service;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.micropoplar.models.crawl.domain.OneNNNRecordListRaw;
import com.micropoplar.models.crawl.repository.OneNNNRecordListRawRepository;

/**
 * 1999列表页面爬取服务。
 * 
 * @author ruixiang
 *
 */
@Service
public class OneNNNCrawlListService {

  @Autowired
  private EntityManager em;

  @Autowired
  private OneNNNRecordListRawRepository repo;

  /**
   * 判断当前爬取记录是否和历史爬取记录相等。
   * 
   * @param nowRecord
   * @return
   */
  public boolean isTheSameWithLatestRawListItem(OneNNNRecordListRaw nowRecord) {
    try {
      OneNNNRecordListRaw latestRecord =
          em.createNamedQuery(OneNNNRecordListRaw.FIND_BY_SN, OneNNNRecordListRaw.class)
              .setParameter(1, nowRecord.getSn()).setMaxResults(1).getSingleResult();

      return nowRecord.equals(latestRecord);
    } catch (NoResultException nre) {
      return false;
    }
  }

  /**
   * 保存列表项目爬取记录。
   * 
   * @param record
   * @return
   */
  public OneNNNRecordListRaw saveRecord(OneNNNRecordListRaw record) {
    return repo.save(record);
  }

}
