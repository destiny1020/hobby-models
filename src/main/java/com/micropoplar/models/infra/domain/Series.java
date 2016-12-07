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
 * 系列实体类型。
 * 
 * @author ruixiang
 *
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "models_infra_series")
public class Series {

  public Series(String seriesName) {
    this.name = seriesName;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /**
   * 系列名称
   */
  @Column(length = 30, nullable = false, unique = true)
  private String name;

}
