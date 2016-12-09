package com.micropoplar.models.resource.dto.req;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import cz.jirutka.validator.collection.constraints.EachURL;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 加密URL资源请求体。
 * 
 * @author ruixiang
 *
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReqUrlEncrypt {

  @Size(min = 1)
  @NotNull
  @EachURL
  private List<String> urls;

  @NotEmpty
  private String prefix;

}
