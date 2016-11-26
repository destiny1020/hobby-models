package com.micropoplar.models.crawl.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 1999爬取的目标页面。
 * 
 * @author ruixiang
 *
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "models_crawl_1999_target")
public class OneNNNCrawlTarget {

  public OneNNNCrawlTarget(Integer sn, String name, String scale) {
    this.sn = sn;
    this.name = name;
    this.scale = scale;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /**
   * 站点分类代码
   */
  @Column
  private Integer sn;

  /**
   * 类型
   */
  @Column
  private String type;

  /**
   * 分类名称
   */
  @Column
  private String name;

  /**
   * 比例
   */
  @Column
  private String scale;

}
