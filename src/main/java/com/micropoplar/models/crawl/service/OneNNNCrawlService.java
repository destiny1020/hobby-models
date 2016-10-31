package com.micropoplar.models.crawl.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 1999.co.jp抓取服务。
 * 
 * @author ruixiang
 *
 */
@Service
public class OneNNNCrawlService {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

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

    Element title = doc.select(OneNNNCrawlConstant.SEL_TITLE).first();
    System.out.println(title.text());
  }

  private static class OneNNNCrawlConstant {

    // 选择器定义
    public static final String SEL_TITLE = "#masterBody_cellHeaderTitle strong";

  }

}
