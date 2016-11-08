package com.micropoplar.models.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.micropoplar.infra.imagesdk.service.IImageManager;
import com.micropoplar.infra.imagesdk.service.impl.QiniuImageManager;

/**
 * Bean声明。
 * 
 * @author ruixiang
 *
 */
@Configuration
@EnableJpaAuditing
public class CommonConfig {

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
