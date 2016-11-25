package com.micropoplar.models.crawl.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.micropoplar.infra.imagesdk.service.IImageManager;

/**
 * 1999抓取服务 - 列表搜索页。
 * 
 * @author ruixiang
 *
 */
@Service
public class OneNNNCrawlListService {

  /**
   * 关键字，页码(从1开始)
   */
  private static final String TMP_ITEM_LIST_URL =
      "http://www.1999.co.jp/search?typ1_c=102&cat=plamo&target=Make&searchkey=%s&spage=%d";

  @Autowired
  private IImageManager imageManager;

  public static void main(String[] args) throws MalformedURLException, IOException {
    String keyword = "タミヤ";
    int page = 1;

    Document doc = Jsoup.parse(new URL(String.format(TMP_ITEM_LIST_URL, keyword, page)), 20000);

    Elements elements = doc.select(OneNNNCrawlListConstant.SEL_LIST_ITEM);
    elements.forEach(element -> {
      System.out.println(element.text());
    });
  }

  /**
   * 获取列表中项目的数量。
   * 
   * @param doc
   * @return
   */
  public int getItemsCount(Document doc) {
    return doc.select(OneNNNCrawlListConstant.SEL_LIST_ITEM).size();
  }

  /**
   * 判断是否还存在下一页。
   * 
   * @param doc
   * @return
   */
  public boolean hasNextPage(Document doc) {
    return doc.select(OneNNNCrawlListConstant.SEL_LIST_HAS_NEXT).size() > 0;
  }

  public static class OneNNNCrawlListConstant {

    // 选择器定义
    // 获取列表中所有商品行
    public static final String SEL_LIST_ITEM = "#divListRow > table";
    // 获取是否还有下一页 - 如果获得的元素length大于0则表示还有下一页
    public static final String SEL_LIST_HAS_NEXT = ".list_kensu00 .list_kensu06 ~ .list_kensu07";

    // 列表元素获取
    public static final String SEL_LIST_ITEM_COVER = "tbody > tr:first a:first img";
    public static final String SEL_LIST_ITEM_SN_TITLE = "tbody > tr:first > td:last td:eq(1) a"; // 取第一个
    public static final String SEL_LIST_ITEM_ATTRS = "tbody > tr:last table td:eq(1) tr"; // 属性区域
    public static final String SEL_LIST_ITEM_ATTRS_COL_1 = "td:eq(2)"; // 第一列属性值：maker，scale，series，code
    public static final String SEL_LIST_ITEM_ATTRS_COL_2 = "td:eq(5)"; // 第二列属性值：release

  }

}
