package com.micropoplar.models.resource.dto.req;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import cz.jirutka.validator.collection.constraints.EachURL;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 解密URL资源请求体。
 * 
 * @author ruixiang
 *
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReqUrlDecrypt {

  @Size(min = 1)
  @NotNull
  @EachURL
  private List<String> urls;

  /**
   * 0: file; 1: base64
   */
  @NotNull
  private Integer type = 0;

}
