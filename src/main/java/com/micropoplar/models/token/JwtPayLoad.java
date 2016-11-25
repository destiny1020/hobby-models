package com.micropoplar.models.token;

import lombok.Data;

/**
 * Token携带的信息。
 * 
 * @author ruixiang
 *
 */
@Data
public class JwtPayLoad {

  /**
   * 类型
   */
  private JwtType type;

  /**
   * 名称: 用户昵称
   */
  private String name;

  /**
   * ID: 用户ID
   */
  private String id;

  public JwtPayLoad(String id, String name, JwtType type) {
    this.id = id;
    this.name = name;
    this.type = type;
  }

}
