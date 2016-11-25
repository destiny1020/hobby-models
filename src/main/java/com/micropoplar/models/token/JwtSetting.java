package com.micropoplar.models.token;

import javax.annotation.PostConstruct;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * JWT配置项。
 * 
 * @author ruixiang
 *
 */
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtSetting {

  private int expiration;
  private String secretKey;
  private String tokenHeader;
  private String authSalt;
  private boolean enabled;

  public int getExpiration() {
    return expiration;
  }

  public void setExpiration(int expiration) {
    this.expiration = expiration;
  }

  public String getSecretKey() {
    return secretKey;
  }

  public String getTokenHeader() {
    return tokenHeader;
  }

  public void setTokenHeader(String tokenHeader) {
    this.tokenHeader = tokenHeader;
  }

  public String getAuthSalt() {
    return authSalt;
  }

  public void setAuthSalt(String authSalt) {
    this.authSalt = authSalt;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  @PostConstruct
  public void populateSecretKey() {
    // this.secretKey = RandomStringUtils.randomAlphanumeric(32);
    this.secretKey = "1234567890asdfghjkl";
  }

}
