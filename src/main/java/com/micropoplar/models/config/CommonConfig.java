package com.micropoplar.models.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.CorsFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.micropoplar.infra.imagesdk.service.IImageManager;
import com.micropoplar.infra.imagesdk.service.impl.QiniuImageManager;
import com.micropoplar.models.infra.auth.UserContainer;
import com.micropoplar.models.token.JwtService;
import com.micropoplar.models.token.JwtSetting;
import com.micropoplar.models.token.filter.JwtFilter;

/**
 * 一般配置。
 * 
 * @author ruixiang
 *
 */
@Configuration
@EnableScheduling
@EnableCaching
@EnableJpaAuditing(auditorAwareRef = "userContainer")
public class CommonConfig {

  @Autowired
  private JwtSetting jwtSetting;

  @Bean
  public FilterRegistrationBean corsFilterRegistration() {
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowCredentials(true);
    config.addAllowedOrigin("*");
    config.addAllowedHeader("*");
    config.addAllowedMethod("OPTIONS");
    config.addAllowedMethod("HEAD");
    config.addAllowedMethod("GET");
    config.addAllowedMethod("PUT");
    config.addAllowedMethod("POST");
    config.addAllowedMethod("DELETE");
    config.addAllowedMethod("PATCH");
    config.addExposedHeader(jwtSetting.getTokenHeader());
    source.registerCorsConfiguration("/**", config);
    final FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
    bean.setOrder(0);
    return bean;
  }

  /**
   * JWT验证Filter。
   * 
   * @param orgJwtSetting
   * @param orgJwtService
   * @return
   */
  @Bean
  public FilterRegistrationBean jwtFilterRegistration(JwtSetting jwtSetting, JwtService jwtService,
      UserContainer userContainer) {
    FilterRegistrationBean registrationBean = new FilterRegistrationBean();
    registrationBean.setFilter(new JwtFilter(jwtSetting, jwtService, userContainer));
    registrationBean.addUrlPatterns("/*");
    registrationBean.setEnabled(true);
    registrationBean.setOrder(1);

    return registrationBean;
  }

  @Bean
  public CharacterEncodingFilter characterEncodingFilter() {
    CharacterEncodingFilter filter = new CharacterEncodingFilter();
    filter.setEncoding("UTF-8");
    filter.setForceEncoding(true);
    return filter;
  }

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
