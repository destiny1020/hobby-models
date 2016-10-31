package com.micropoplar.models.crawl.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 1999.co.jp模型记录(不添加任何加工)。
 * 
 * @author ruixiang
 *
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "models_crawl_1999_record_raw")
public class OneNNNRecordRaw {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /**
   * 商品站点SN
   */
  @Column(length = 40)
  private String sn;

  /**
   * 商品名称
   */
  @Column(length = 255)
  private String title;

  /**
   * 商品制造商
   */
  @LazyCollection(LazyCollectionOption.FALSE)
  @ElementCollection
  @CollectionTable(name = "models_crawl_1999_record_raw_maker",
      joinColumns = @JoinColumn(name = "item_id"))
  @Column(name = "maker")
  private List<String> makers;

  /**
   * 比例
   */
  @Column(length = 20)
  private String scale;

  /**
   * 系列
   */
  @LazyCollection(LazyCollectionOption.FALSE)
  @ElementCollection
  @CollectionTable(name = "models_crawl_1999_record_raw_series",
      joinColumns = @JoinColumn(name = "item_id"))
  @Column(name = "series")
  private List<String> series;

  /**
   * 发售日
   */
  @Column(name = "release_date_raw", length = 40)
  private String releaseDateRaw;

  /**
   * 站点记录的商品代码
   */
  @Column(length = 20)
  private String code;

  /**
   * 所属国家
   */
  @Column(length = 40)
  private String country;

  /**
   * 模型标签
   */
  @LazyCollection(LazyCollectionOption.FALSE)
  @ElementCollection
  @CollectionTable(name = "models_crawl_1999_record_raw_tag",
      joinColumns = @JoinColumn(name = "item_id"))
  @Column(name = "tag")
  private List<String> tags;

  /**
   * 模型图片
   */
  @LazyCollection(LazyCollectionOption.FALSE)
  @ElementCollection(targetClass = OneNNNRecordImage.class)
  @CollectionTable(name = "models_crawl_1999_record_raw_image",
      joinColumns = @JoinColumn(name = "item_id"))
  private List<String> images;

  /**
   * 创建时间
   */
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "create_time")
  private Date createTime;

  /**
   * 更新时间
   */
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "update_time")
  private Date updateTime;

}
