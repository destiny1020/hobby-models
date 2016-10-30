package com.micropoplar.models.search.dto;

import java.util.Map;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RespSearchResult {

  public RespSearchResult(Map<String, Object> source) {
    this.category = (String) source.get("category");
    this.url = (String) source.get("detail_url");
    this.imageUrl = (String) source.get("pic_url");
    this.price = (String) source.get("view_price");
    this.deliveryPrice = (String) source.get("view_fee");
    this.buyCount = (String) source.get("view_sales");
    this.title = (String) source.get("title");
    this.seller = (String) source.get("nick");
    this.location = (String) source.get("item_loc");
    this.commentCount = (String) source.get("comment_count");
    this.commentUrl = (String) source.get("comment_url");
    this.shopUrl = (String) source.get("shopLink");
  }

  private String category;
  private String url;
  private String imageUrl;
  private String price;
  private String deliveryPrice;
  private String buyCount;
  private String title;
  private String seller;
  private String location;
  private String commentCount;
  private String commentUrl;
  private String shopUrl;

}
