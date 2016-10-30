package com.micropoplar.models.search.dto.resp;

import java.util.List;

import com.micropoplar.models.search.dto.req.ReqSearchParam;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 搜索结果响应体。
 * 
 * @author ruixiang
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RespSearchResultWrapper {

  private ReqSearchParam param;
  private Integer count;
  private List<RespSearchResult> results;

}
