package com.micropoplar.models.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * Bean声明。
 * 
 * @author ruixiang
 *
 */
@Configuration
public class CommonConfig {

  @Bean
  public ObjectMapper mapper() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.disable(SerializationFeature.INDENT_OUTPUT);
    return mapper;
  }

}
