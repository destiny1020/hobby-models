package com.micropoplar.models.crawl.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.micropoplar.models.common.response.AuditingEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 1999模型列表实体(不添加任何加工)。
 * 
 * @author ruixiang
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Entity
@Table(name = "models_crawl_1999_record_list_raw")
public class OneNNNRecordListRaw extends AuditingEntity {

  public OneNNNRecordListRaw(String coverUrl, String sn, String title, String makers,
      String releaseDate, String scales, String series, String code) {
    this.coverUrl = coverUrl;
    this.sn = sn;
    this.title = title;
    this.makers = makers;
    this.release = releaseDate;
    this.scales = scales;
    this.series = series;
    this.code = code;
  }

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
  @Column(length = 255, name = "makers")
  private String makers;

  /**
   * 比例
   */
  @Column(length = 255, name = "scales")
  private String scales;

  /**
   * 系列
   */
  @Column(length = 255, name = "series")
  private String series;

  /**
   * 代码
   */
  @Column(length = 255, name = "code")
  private String code;

  /**
   * 发售/预定时间
   */
  @Column(length = 255, name = "release_date")
  private String release;

}
