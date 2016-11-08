package com.micropoplar.models.crawl.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.micropoplar.models.crawl.service.biz.OneNNNImageMetadata;

import lombok.NoArgsConstructor;

/**
 * 1999商品包含的图片信息。
 * 
 * @author ruixiang
 *
 */
@Embeddable
@NoArgsConstructor
public class OneNNNRecordImage {

  public OneNNNRecordImage(OneNNNImageMetadata meta) {
    this.seq = meta.getIdx();
    this.type = meta.getType();
    this.subSeq = meta.getIdxOfType();
    this.originalSmallUrl = meta.getSmallImageUrl();
    this.originalLargeUrl = meta.getLargeImageUrl();
    this.localSmallName = meta.getSmallLocalImageName();
    this.localLargeName = meta.getLargeLocalImageName();
    this.qiniuSmallUrl = meta.getSmallQiniuImageUrl();
    this.qiniuLargeUrl = meta.getLargeQiniuImageUrl();
  }

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
   * 原地址 - 小图
   */
  @Column(name = "original_small_url")
  private String originalSmallUrl;

  /**
   * 原地址 - 大图
   */
  @Column(name = "original_large_url")
  private String originalLargeUrl;

  /**
   * 本地文件名 - 小图
   */
  @Column(name = "local_small_name")
  private String localSmallName;

  /**
   * 本地文件名 - 大图
   */
  @Column(name = "local_large_name")
  private String localLargeName;

  /**
   * 七牛地址 - 小图
   */
  @Column(name = "qiniu_small_url")
  private String qiniuSmallUrl;

  /**
   * 七牛地址 - 大图
   */
  @Column(name = "qiniu_large_url")
  private String qiniuLargeUrl;

}
