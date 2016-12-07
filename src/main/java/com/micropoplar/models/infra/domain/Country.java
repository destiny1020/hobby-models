package com.micropoplar.models.infra.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 国家元数据实体类型。
 * 
 * @author ruixiang
 *
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "models_infra_country")
public class Country {

  public Country(String nameJapanese) {
    this.nameJapanese = nameJapanese;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /**
   * 国家代码
   */
  @Column(length = 10, nullable = true, unique = true)
  private String code;

  /**
   * 国家名称中文
   */
  @Column(length = 100, nullable = true, unique = true)
  private String name;

  /**
   * 国家名称日文
   */
  @Column(name = "name_jap", length = 100, nullable = true, unique = true)
  private String nameJapanese;

  /**
   * 国家名称英文
   */
  @Column(name = "name_eng", length = 100, nullable = true, unique = true)
  private String nameEnglish;

}
