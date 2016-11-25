package com.micropoplar.models.token;

/**
 * 系统角色种类。
 * 
 * @author ruixiang
 *
 */
public enum JwtType {

  // @formatter:off
  SA("超级管理员"),            // 超级管理员 - 预留
  MEMBER("会员");             // 会员
  // @formatter:on

  private String role;

  private JwtType(String role) {
    this.role = role;
  }

  public String getRole() {
    return role;
  }

}
