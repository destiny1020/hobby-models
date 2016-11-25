package com.micropoplar.models.infra.auth;

import com.micropoplar.models.infra.domain.User;
import com.micropoplar.models.token.JwtType;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 代表当前执行操作的用户。
 * 
 * @author ruixiang
 *
 */
@Data
@NoArgsConstructor
public class UserToken {

  public UserToken(User user) {
    this.id = user.getId();
    this.name = user.getName();
    this.role = user.getRole();
  }

  private Long id;
  private String name;
  private JwtType role;

}
