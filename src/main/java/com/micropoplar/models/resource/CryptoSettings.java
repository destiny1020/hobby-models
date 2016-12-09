package com.micropoplar.models.resource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 用于加解密图片的配置。
 * 
 * @author ruixiang
 *
 */
@Component
@ConfigurationProperties(prefix = "crypto")
public class CryptoSettings {

  private String secret;

  public String getSecret() {
    return secret;
  }

  public void setSecret(String secret) {
    this.secret = secret;
  }

}
