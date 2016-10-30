package com.micropoplar.models.search.dto.req;

/**
 * 搜索排序方式枚举。
 * 
 * @author ruixiang
 *
 */
public enum ReqSearchSort {

  // @formatter:off
  PRICE_ASC("price-asc"),
  PRICE_DESC("price-desc"),
  SALE_DESC("sale-desc"),
  CREDIT_DESC("credit-desc"),
  DEFAULT("default");
  // @formatter:on

  private String sort;

  private ReqSearchSort(String sort) {
    this.sort = sort;
  }

  public String getSort() {
    return this.sort;
  }

  public static ReqSearchSort nameOf(String sort) {
    switch (sort) {
      case "credit-desc":
        return ReqSearchSort.CREDIT_DESC;
      case "sale-desc":
        return ReqSearchSort.SALE_DESC;
      case "price-desc":
        return ReqSearchSort.PRICE_DESC;
      case "price-asc":
        return ReqSearchSort.PRICE_ASC;
      case "default":
      default:
        return ReqSearchSort.DEFAULT;
    }
  }
}
