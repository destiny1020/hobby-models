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
 * 比例实体类型。
 * 
 * @author ruixiang
 *
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "models_infra_scale")
public class Scale {

  public Scale(String scaleName) {
    this.name = scaleName;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /**
   * 比例名称
   */
  @Column(length = 30, nullable = false, unique = true)
  private String name;

}
