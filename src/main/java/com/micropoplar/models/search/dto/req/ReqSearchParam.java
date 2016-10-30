package com.micropoplar.models.search.dto.req;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ReqSearchParam {

  private String keyword = "";
  private Integer offset = 0;
  private Integer size = 44;
  private ReqSearchSort sort = ReqSearchSort.DEFAULT;

  public void fix() {
    if (offset < 0) {
      offset = 0;
    }

    if (size <= 0 || size > 44) {
      size = 44;
    }
  }

  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }

  public void setOffset(Integer offset) {
    this.offset = offset;
  }

  public void setSize(Integer size) {
    this.size = size;
  }

  public void setSort(String sort) {
    this.sort = ReqSearchSort.nameOf(sort);
  }

  public String getKeyword() {
    return keyword;
  }

  public Integer getOffset() {
    return offset;
  }

  public Integer getSize() {
    return size;
  }

  public String getSort() {
    return sort.getSort();
  }

}
