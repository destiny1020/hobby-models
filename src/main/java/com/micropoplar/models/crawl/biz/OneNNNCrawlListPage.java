package com.micropoplar.models.crawl.biz;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.stream.Collectors;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.micropoplar.models.crawl.constant.CrawlContant;
import com.micropoplar.models.crawl.domain.OneNNNRecordListRaw;
import com.micropoplar.models.crawl.util.CrawlConnectionUtil;
import com.micropoplar.models.crawl.util.OneNNNCrawlerUtil;

/**
 * 1999抓取服务 - 列表页。
 * 
 * @author ruixiang
 *
 */
public class OneNNNCrawlListPage {

  /**
   * 关键字，页码(从1开始) - SN - Page
   */
  private static final String TMP_ITEM_LIST_URL =
      CrawlContant.CRAWL_1999_SITE_BASE + "/list/%s/0/%d";

  private static final Logger LOGGER = LoggerFactory.getLogger(OneNNNCrawlListPage.class);

  private Document doc;
  private String sn;
  private int page;

  private OneNNNCrawlListPage(String sn, int page) {
    this.sn = sn;
    this.page = page;
  }

  public OneNNNCrawlListPage(String sn) {
    this.sn = sn;
    this.page = 0;
  }

  /**
   * 访问链接获取HTML。
   * 
   * @throws IOException
   * @throws MalformedURLException
   */
  public void connect() throws MalformedURLException, IOException {
    LOGGER.info(String.format("[爬虫 - 1999] 正在爬取列表页: %s - 页码: %d", sn, page));
    doc = CrawlConnectionUtil.getDocument(String.format(TMP_ITEM_LIST_URL, sn, page));
  }

  /**
   * 跳转到下一页。
   * 
   * @return
   */
  public OneNNNCrawlListPage navToNext() {
    if (hasNextPage()) {
      return new OneNNNCrawlListPage(sn, page + 1);
    } else {
      throw new RuntimeException("没有下一页了");
    }
  }

  public static void main(String[] args) throws MalformedURLException, IOException {
    String keyword = "タミヤ";
    int page = 1;

    Document doc = CrawlConnectionUtil.getDocument(String.format(TMP_ITEM_LIST_URL, keyword, page));

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
  public int getItemsCount() {
    return getDoc().select(OneNNNCrawlListConstant.SEL_LIST_ITEM).size();
  }

  /**
   * 解析列表项目。
   * 
   * @param doc
   * @return
   */
  public List<OneNNNRecordListRaw> parseItems() {
    Elements items = getDoc().select(OneNNNCrawlListConstant.SEL_LIST_ITEM);
    LOGGER.info(String.format("[爬虫 - 1999] 正在分析: %s - 页码: %d 的 %d 条记录", sn, page, items.size()));
    return items.stream().map(item -> {
      // 封绘URL
      Element cover = item.select(OneNNNCrawlListConstant.SEL_LIST_ITEM_COVER).first();
      String coverUrl = OneNNNCrawlerUtil.addPrefix(cover.attr("src"));

      // SN以及商品名称
      Element snAndTitle = item.select(OneNNNCrawlListConstant.SEL_LIST_ITEM_SN_TITLE).first();
      String sn = snAndTitle.attr("href").substring(1);
      String title = snAndTitle.text().trim();

      // 各种属性
      Elements attrs = item.select(OneNNNCrawlListConstant.SEL_LIST_ITEM_ATTRS);

      // 第一行 - makers & releaseDate
      String makers =
          attrs.get(0).select(OneNNNCrawlListConstant.SEL_LIST_ITEM_ATTRS_COL_1).text().trim();
      String releaseDate =
          attrs.get(0).select(OneNNNCrawlListConstant.SEL_LIST_ITEM_ATTRS_COL_2).text().trim();

      // 第二行 - scale
      String scales =
          attrs.get(1).select(OneNNNCrawlListConstant.SEL_LIST_ITEM_ATTRS_COL_1).text().trim();

      // 第三行 - series
      String series =
          attrs.get(2).select(OneNNNCrawlListConstant.SEL_LIST_ITEM_ATTRS_COL_1).text().trim();

      // 第四行 - code
      String code =
          attrs.get(3).select(OneNNNCrawlListConstant.SEL_LIST_ITEM_ATTRS_COL_1).text().trim();

      return new OneNNNRecordListRaw(coverUrl, sn, title, makers, releaseDate, scales, series,
          code);
    }).collect(Collectors.toList());
  }

  /**
   * 判断是否还存在下一页。
   * 
   * @param doc
   * @return
   */
  public boolean hasNextPage() {
    if (page == 0) {
      return Boolean.TRUE;
    }

    return getDoc().select(OneNNNCrawlListConstant.SEL_LIST_HAS_NEXT).size() > 0;
  }

  public Document getDoc() {
    return this.doc;
  }

  public static class OneNNNCrawlListConstant {

    // 选择器定义
    // 获取列表中所有商品行
    public static final String SEL_LIST_ITEM = "#divListRow > table";
    // 获取是否还有下一页 - 如果获得的元素length大于0则表示还有下一页
    public static final String SEL_LIST_HAS_NEXT = ".list_kensu00 .list_kensu06 ~ .list_kensu07";

    // 列表元素获取
    public static final String SEL_LIST_ITEM_COVER = "tbody > tr:eq(0) a:eq(0) img";
    public static final String SEL_LIST_ITEM_SN_TITLE = "tbody > tr:eq(0) > td:eq(1) td:eq(1) a"; // 取第一个
    public static final String SEL_LIST_ITEM_ATTRS = "tbody > tr:eq(1) table td:eq(1) tr"; // 属性区域
    public static final String SEL_LIST_ITEM_ATTRS_COL_1 = "td:eq(2)"; // 第一列属性值：maker，scale，series，code
    public static final String SEL_LIST_ITEM_ATTRS_COL_2 = "td:eq(5)"; // 第二列属性值：release

  }

}
