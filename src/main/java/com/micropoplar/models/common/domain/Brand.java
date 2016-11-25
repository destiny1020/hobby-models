package com.micropoplar.models.common.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 品牌实体类型。
 * 
 * @author ruixiang
 *
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "models_common_brand")
public class Brand {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /**
   * 品牌代码
   */
  @Column(length = 10, nullable = false, unique = true)
  private String code;

  /**
   * 品牌名称中文
   */
  @Column(length = 100, nullable = true, unique = true)
  private String name;

  /**
   * 品牌名称日文
   */
  @Column(name = "name_jap", length = 100, nullable = true, unique = true)
  private String nameJapanese;

  /**
   * 品牌名称英文
   */
  @Column(name = "name_eng", length = 100, nullable = true, unique = true)
  private String nameEnglish;

}
