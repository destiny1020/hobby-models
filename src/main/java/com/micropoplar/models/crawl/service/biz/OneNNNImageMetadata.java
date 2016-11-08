package com.micropoplar.models.crawl.service.biz;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商品页面抓取图片元数据供后续实际抓取过程使用。
 * 
 * @author ruixiang
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OneNNNImageMetadata {

  private int idx;
  private String type;
  private int idxOfType;

  // 源文件URL
  private String smallImageUrl;
  private String largeImageUrl;

  // 本地文件名
  private String smallLocalImageName;
  private String largeLocalImageName;

  // 七牛URL
  private String smallQiniuImageUrl;
  private String largeQiniuImageUrl;

}
