package com.micropoplar.models.crawl.domain;

import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.micropoplar.models.common.response.AuditingEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 1999模型详情实体(不添加任何加工)。
 * 
 * @author ruixiang
 *
 */
@Data
@EqualsAndHashCode(callSuper = false, exclude = {"id", "images", "latest"})
@NoArgsConstructor
@Entity
@Table(name = "models_crawl_1999_record_raw")
@NamedQueries(@NamedQuery(name = OneNNNRecordRaw.FIND_BY_SN, query = OneNNNRecordRaw.FIND_BY_SN_QL))
public class OneNNNRecordRaw extends AuditingEntity {

  public static final String FIND_BY_SN = "OneNNNRecordRaw.findBySn";
  public static final String FIND_BY_SN_QL =
      "select r from OneNNNRecordRaw r where r.sn = ?1 order by r.createdTime desc";

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
   * 商品封绘URL
   */
  @Column(length = 255, name = "cover_url")
  private String coverUrl;

  /**
   * 商品制造商
   */
  @LazyCollection(LazyCollectionOption.FALSE)
  @ElementCollection
  @CollectionTable(name = "models_crawl_1999_record_raw_maker",
      joinColumns = @JoinColumn(name = "item_id"))
  @Column(name = "maker", length = 50)
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
  @Column(name = "series", length = 50)
  private List<String> series;

  /**
   * 发售日
   */
  @Column(name = "release_date_raw", length = 40)
  private String releaseDateRaw;

  /**
   * 发售预定日
   */
  @Column(name = "release_reserve_date_raw", length = 40)
  private String releaseReserveDateRaw;

  /**
   * 站点记录的商品代码
   */
  @Column(length = 20)
  private String code;

  /**
   * 所属国家
   */
  @LazyCollection(LazyCollectionOption.FALSE)
  @ElementCollection
  @CollectionTable(name = "models_crawl_1999_record_raw_country",
      joinColumns = @JoinColumn(name = "item_id"))
  @Column(name = "country", length = 50)
  private List<String> countries;

  /**
   * 模型图片
   */
  @LazyCollection(LazyCollectionOption.FALSE)
  @ElementCollection(targetClass = OneNNNRecordImage.class)
  @CollectionTable(name = "models_crawl_1999_record_raw_image",
      joinColumns = @JoinColumn(name = "item_id"))
  private List<OneNNNRecordImage> images;

  /**
   * 是否是最新的RAW记录
   */
  @Column(name = "is_latest", nullable = false)
  private Boolean latest = Boolean.TRUE;

  @Transient
  private Boolean shouldSave = false;

}
