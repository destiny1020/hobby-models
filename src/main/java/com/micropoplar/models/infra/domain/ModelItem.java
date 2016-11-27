package com.micropoplar.models.infra.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 模型实体类型。
 * 
 * @author ruixiang
 *
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "models_infra_model_item")
public class ModelItem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /**
   * 对应品牌
   */
  @JoinTable(name = "models_infra_model_item_has_brand",
      joinColumns = {@JoinColumn(name = "item_id", referencedColumnName = "id")},
      inverseJoinColumns = {@JoinColumn(name = "brand_id", referencedColumnName = "id")})
  @ManyToMany(fetch = FetchType.LAZY)
  private List<Brand> brands;

}
