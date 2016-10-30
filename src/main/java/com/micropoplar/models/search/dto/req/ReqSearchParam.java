package com.micropoplar.models.search.dto.req;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReqSearchParam {

  private String keyword = "";
  private Integer offset = 0;
  private Integer size = 44;

  public void fix() {
    if (offset < 0) {
      offset = 0;
    }

    if (size <= 0 || size > 44) {
      size = 44;
    }
  }

}
