package com.micropoplar.models.crawl.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.micropoplar.models.crawl.domain.OneNNNRecordRaw;

/**
 * 1999.co.jp抓取服务。
 * 
 * @author ruixiang
 *
 */
@Service
public class OneNNNCrawlService {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private static final String TMP_ITEM_BASE = "http://www.1999.co.jp";
  private static final String TMP_ITEM_URL = "http://www.1999.co.jp/%s/";
  private static final String TMP_ITEM_IMAGES_URL = "http://www.1999.co.jp/image/%s/10/0";

  public static void main(String[] args) throws MalformedURLException, IOException {
    Document doc =
        Jsoup.parse(new URL(String.format("http://www.1999.co.jp/image/10088591/10/0")), 20000);

    Elements images = doc.select("#imgAll img");
    images.forEach(image -> {
      System.out.println(image.attr("src"));
    });
  }

  public void crawl(String sn) throws MalformedURLException, IOException {
    logger.info("[爬虫]商品SN: " + sn);

    Document doc = Jsoup.connect(String.format(TMP_ITEM_URL, sn))
        .userAgent(
            "Mozilla/5.0 (X11; U; Linux i586; en-US; rv:1.7.3) Gecko/20040924 Epiphany/1.4.4 (Ubuntu)")
        .timeout(50000).get();

    OneNNNRecordRaw record = new OneNNNRecordRaw();
    record.setSn(sn);

    // 商品名称
    Element titleElem = doc.select(OneNNNCrawlConstant.SEL_TITLE).first();
    String title = titleElem.text();
    record.setTitle(title);
    System.out.println(title);

    // 封绘小图
    Element coverElem = doc.select(OneNNNCrawlConstant.SEL_COVER).first();
    String coverUrl = coverElem.attr("src");
    if (coverUrl.startsWith("/")) {
      coverUrl = TMP_ITEM_BASE + coverUrl;
    }
    record.setCoverUrl(coverUrl);
    System.out.println(coverUrl);

    // 制造商，比例，发售日，商品代码
    Elements meta1Elem = doc.select(OneNNNCrawlConstant.SEL_META_1);
    Map<String, List<String>> meta1Map = meta1Elem.stream().collect(Collectors.toMap(row -> {
      return row.select("td").first().text();
    }, row -> {
      Element linkElem = row.select("td").last();
      return linkElem.select("a").stream().map(Element::text).collect(Collectors.toList());
    }));

    // 制造商
    List<String> makers = meta1Map.get(OneNNNCrawlConstant.KEY_MAKER);
    if (makers != null && makers.size() > 0) {
      record.setMakers(makers);
      System.out.println(makers);
    }

    // 比例
    List<String> scales = meta1Map.get(OneNNNCrawlConstant.KEY_SCALE);
    if (scales != null && scales.size() == 1) {
      record.setScale(scales.get(0));
      System.out.println(scales);
    }

    // 发售日
    List<String> releaseDates = meta1Map.get(OneNNNCrawlConstant.KEY_RELEASE_RESERVE_DATE);
    if (releaseDates != null && releaseDates.size() == 1) {
      record.setReleaseDateRaw(releaseDates.get(0));
      System.out.println(releaseDates);
    }

    // 发售预定日
    List<String> releaseReserveDates = meta1Map.get(OneNNNCrawlConstant.KEY_RELEASE_DATE);
    if (releaseReserveDates != null && releaseReserveDates.size() == 1) {
      record.setReleaseReserveDateRaw(releaseReserveDates.get(0));
      System.out.println(releaseReserveDates);
    }

    // 商品代码
    List<String> codes = meta1Map.get(OneNNNCrawlConstant.KEY_CODE);
    if (codes != null && codes.size() == 1) {
      record.setCode(codes.get(0));
      System.out.println(codes);
    }

    // 商品系列，国
    Elements meta2Elem = doc.select(OneNNNCrawlConstant.SEL_META_2);
    Map<String, List<String>> meta2Map = meta2Elem.stream().collect(Collectors.toMap(row -> {
      return row.select("td").get(1).text();
    }, row -> {
      Element linkElem = row.select("td").last();
      return linkElem.select("a").stream().map(Element::text).collect(Collectors.toList());
    }));

    // 商品系列
    List<String> series = meta2Map.get(OneNNNCrawlConstant.KEY_SERIES);
    if (series != null && series.size() > 0) {
      record.setSeries(series);
      System.out.println(series);
    }

    // 国家
    List<String> countries = meta1Map.get(OneNNNCrawlConstant.KEY_COUNTRY);
    if (countries != null && countries.size() > 0) {
      record.setCountries(countries);
      System.out.println(countries);
    }

  }

  private static class OneNNNCrawlConstant {

    // 选择器定义
    // 商品名称
    public static final String SEL_TITLE = "#masterBody_cellHeaderTitle strong";
    // 封绘小图
    public static final String SEL_COVER = "#masterBody_imgMain";
    // 制造商，比例，发售日，商品代码
    public static final String SEL_META_1 = "#tblItemInfo tr";
    // 商品系列，国家
    public static final String SEL_META_2 = "table.tag01 tr";

    // 元数据Map Key定义
    // 制造商
    public static final String KEY_MAKER = "メーカー";
    // 比例
    public static final String KEY_SCALE = "スケール";
    // 发售日
    public static final String KEY_RELEASE_DATE = "発売日";
    // 发售预定日
    public static final String KEY_RELEASE_RESERVE_DATE = "発売予定日";
    // 商品代码
    public static final String KEY_CODE = "商品コード";
    // 商品系列
    public static final String KEY_SERIES = "商品シリーズ";
    // 国家
    public static final String KEY_COUNTRY = "国";

  }

}
