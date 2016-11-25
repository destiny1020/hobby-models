package com.micropoplar.models.infra.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.micropoplar.models.token.JwtType;

import lombok.Data;

/**
 * 用户实体。
 * 
 * @author ruixiang
 *
 */
@Data
@Entity
@Table(name = "models_infra_user")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /**
   * 用户名
   */
  @Column(name = "name")
  private String name;

  /**
   * 用户角色
   */
  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private JwtType role;

}
