package com.micropoplar.models.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.micropoplar.infra.imagesdk.service.IImageManager;
import com.micropoplar.infra.imagesdk.service.impl.QiniuImageManager;
import com.micropoplar.models.token.JwtSetting;

/**
 * 一般配置。
 * 
 * @author ruixiang
 *
 */
@Configuration
@EnableScheduling
@EnableCaching
@EnableJpaAuditing
public class CommonConfig {

  @Autowired
  private JwtSetting jwtSetting;

  @Bean
  public ObjectMapper mapper() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.disable(SerializationFeature.INDENT_OUTPUT);
    return mapper;
  }

  @Bean
  public IImageManager imageManager() {
    return new QiniuImageManager();
  }

}
