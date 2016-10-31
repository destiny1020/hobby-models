package com.micropoplar.models.crawl.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * 1999商品包含的图片信息。
 * 
 * @author ruixiang
 *
 */
@Embeddable
public class OneNNNRecordImage {

  /**
   * 图片序号
   */
  @Column
  private Integer seq;

  /**
   * 图片类型
   */
  @Column
  private String type;

  /**
   * 图片类型内序号
   */
  @Column(name = "sub_seq")
  private Integer subSeq;

  /**
   * 原地址
   */
  @Column(name = "original_url")
  private String originalUrl;

  /**
   * 本地文件名
   */
  @Column(name = "local_name")
  private String localName;

  /**
   * 七牛KEY
   */
  @Column(name = "qiniu_key")
  private String qiniuKey;

  /**
   * 七牛地址
   */
  @Column(name = "qiniu_url")
  private String qiniuUrl;

}
