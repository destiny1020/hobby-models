package com.micropoplar.models.crawl.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.micropoplar.infra.imagesdk.service.IImageManager;
import com.micropoplar.infra.imagesdk.service.IResponse;
import com.micropoplar.models.crawl.domain.OneNNNRecordImage;
import com.micropoplar.models.crawl.domain.OneNNNRecordRaw;
import com.micropoplar.models.crawl.service.biz.OneNNNImageMetadata;
import com.micropoplar.models.repository.OneNNNRawRecordRepository;

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

  @Autowired
  private IImageManager imageManager;

  @Autowired
  private OneNNNRawRecordRepository rawRecordRepo;

  public static void main(String[] args) throws MalformedURLException, IOException {
    Document doc =
        Jsoup.parse(new URL(String.format("http://www.1999.co.jp/image/10088591/10/0")), 20000);

    Elements images = doc.select("#imgAll img");
    images.forEach(image -> {
      System.out.println(image.attr("src"));
    });
  }

  public void crawl(String sn) throws MalformedURLException, IOException {
    logger.info("[爬虫]商品详情页: " + sn);

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
    String coverUrl = addPrefix(coverElem.attr("src"));
    record.setCoverUrl(coverUrl);
    System.out.println(coverUrl);

    // 制造商，比例，发售日，商品代码
    Elements meta1Elem = doc.select(OneNNNCrawlConstant.SEL_META_1);
    Map<String, List<String>> meta1Map = meta1Elem.stream().collect(Collectors.toMap(row -> {
      return row.select("td").first().text();
    }, row -> {
      Element linkElem = row.select("td").last();
      Elements hrefs = linkElem.select("a");
      if (hrefs != null && hrefs.size() > 0) {
        return hrefs.stream().map(Element::text).collect(Collectors.toList());
      } else {
        return Arrays.asList(linkElem.text());
      }
    }));

    // 制造商
    List<String> makers = meta1Map.get(OneNNNCrawlConstant.KEY_MAKER);
    if (makers != null && makers.size() > 0) {
      record.setMakers(makers);
      logger.info(String.format("[爬虫 - 基本] SN: %s, 制造商: %s", sn, makers));
    }

    // 比例
    List<String> scales = meta1Map.get(OneNNNCrawlConstant.KEY_SCALE);
    if (scales != null && scales.size() == 1) {
      record.setScale(scales.get(0));
      logger.info(String.format("[爬虫 - 基本] SN: %s, 比例: %s", sn, scales));
    }

    // 发售日
    List<String> releaseDates = meta1Map.get(OneNNNCrawlConstant.KEY_RELEASE_RESERVE_DATE);
    if (releaseDates != null && releaseDates.size() == 1) {
      record.setReleaseDateRaw(releaseDates.get(0));
      logger.info(String.format("[爬虫 - 基本] SN: %s, 发售日: %s", sn, releaseDates));
    }

    // 发售预定日
    List<String> releaseReserveDates = meta1Map.get(OneNNNCrawlConstant.KEY_RELEASE_DATE);
    if (releaseReserveDates != null && releaseReserveDates.size() == 1) {
      record.setReleaseReserveDateRaw(releaseReserveDates.get(0));
      logger.info(String.format("[爬虫 - 基本] SN: %s, 发售预定日: %s", sn, releaseReserveDates));
    }

    // 商品代码
    List<String> codes = meta1Map.get(OneNNNCrawlConstant.KEY_CODE);
    if (codes != null && codes.size() == 1) {
      System.out.println(codes);
      logger.info(String.format("[爬虫 - 基本] SN: %s, 商品代码: %s", sn, codes));
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
      logger.info(String.format("[爬虫 - 基本] SN: %s, 系列: %s", sn, series));
    }

    // 国家
    List<String> countries = meta2Map.get(OneNNNCrawlConstant.KEY_COUNTRY);
    if (countries != null && countries.size() > 0) {
      record.setCountries(countries);
      logger.info(String.format("[爬虫 - 基本] SN: %s, 国家: %s", sn, countries));
    }

    List<OneNNNImageMetadata> imagesMeta = getImagesMetadata(doc);

    // 访问图片页得到完整的图片信息
    addLargeImageUrl(sn, imagesMeta);
    // imagesMeta.forEach(System.out::println);

    // 创建保存目录
    Path path = Paths.get(String.format("crawler/images/%s", sn));
    Files.createDirectories(path);

    imagesMeta.parallelStream().forEach(meta -> {
      try {
        // 小图
        coreCrawlImage(sn, meta, true);
      } catch (Exception e) {
        e.printStackTrace();
      }

      try {
        // 大图
        coreCrawlImage(sn, meta, false);
      } catch (Exception e) {
        e.printStackTrace();
      }
    });

    // imagesMeta.forEach(System.out::println);
    // 转换成实体类型
    record.setImages(imagesMeta.stream().map(OneNNNRecordImage::new).collect(Collectors.toList()));

    rawRecordRepo.save(record);
  }

  private void coreCrawlImage(String sn, OneNNNImageMetadata meta, boolean isSmall)
      throws FileNotFoundException, IOException, MalformedURLException {
    String localImageName = String.format("crawler/images/%s/%s_%d-%s-%d.jpg", sn,
        isSmall ? "S" : "L", meta.getIdx(), meta.getType(), meta.getIdxOfType());

    // 检查文件是否已经存在
    byte[] imageBytes = null;
    if (!Files.exists(Paths.get(localImageName))) {
      // 还不存在
      String targetUrl = isSmall ? meta.getSmallImageUrl() : meta.getLargeImageUrl();
      logger.info(String.format("[爬虫 - 图片] SN: %s 正在下载%s图: %d - %s", sn, isSmall ? "小" : "大",
          meta.getIdx(), targetUrl));
      FileOutputStream out = new FileOutputStream(localImageName);
      URLConnection conn = new URL(targetUrl).openConnection();
      conn.setConnectTimeout(10000);
      try {
        conn.connect();
        InputStream is = conn.getInputStream();
        imageBytes = IOUtils.toByteArray(is);
        out.write(imageBytes);
        out.flush();
        out.close();
        is.close();
      } catch (SocketTimeoutException ste) {
        logger.error(String.format("[爬虫 - 图片] SN: %s 超时 - 下载%s图: %d - %s", sn, isSmall ? "小" : "大",
            meta.getIdx(), targetUrl));
        throw new RuntimeException("下载图片失败");
      }
    } else {
      // 已经存在
      FileInputStream in = new FileInputStream(localImageName);
      imageBytes = IOUtils.toByteArray(in);
    }

    if (isSmall) {
      meta.setSmallLocalImageName(localImageName);
    } else {
      meta.setLargeLocalImageName(localImageName);
    }

    // 上传图片到七牛
    logger.error(String.format("[爬虫 - 图片] SN: %s 上传%s图到七牛: %d - %s", sn, isSmall ? "小" : "大",
        meta.getIdx(), localImageName));
    String imageUrl = uploadToQiniu(sn, imageBytes);
    if (isSmall) {
      meta.setSmallQiniuImageUrl(imageUrl);
    } else {
      meta.setLargeQiniuImageUrl(imageUrl);
    }
  }

  private String uploadToQiniu(String sn, byte[] imageBytes) {
    String key = "crawler/onennn/" + sn + "/" + UUID.randomUUID().toString();
    IResponse response = imageManager.simpleUpload("models-biz", key, imageBytes);
    String imageUrl = response.getDownloadUrl();
    return imageUrl;
  }

  /**
   * 添加大图信息
   * 
   * @param imagesMeta
   * @throws IOException
   */
  private void addLargeImageUrl(String sn, List<OneNNNImageMetadata> imagesMeta)
      throws IOException {
    logger.info("[爬虫]商品图片页: " + sn);

    Document doc = Jsoup.connect(String.format(TMP_ITEM_IMAGES_URL, sn))
        .userAgent(
            "Mozilla/5.0 (X11; U; Linux i586; en-US; rv:1.7.3) Gecko/20040924 Epiphany/1.4.4 (Ubuntu)")
        .timeout(50000).get();

    Elements largeImages = doc.select(OneNNNCrawlConstant.SEL_LARGE_IMAGES);
    assert imagesMeta.size() == largeImages.size();
    for (int i = 0; i < imagesMeta.size(); i++) {
      imagesMeta.get(i).setLargeImageUrl(addPrefix(largeImages.get(i).attr("src")));
    }
  }

  /**
   * 获取图片信息。
   * 
   * @param doc
   * @return
   */
  private List<OneNNNImageMetadata> getImagesMetadata(Document doc) {
    List<OneNNNImageMetadata> imagesMeta = new LinkedList<>();
    Elements imagesSection = doc.select(OneNNNCrawlConstant.SEL_IMAGES);
    int idx = 1;
    for (int i = 0; i < imagesSection.size(); i++) {
      // 类型部分
      Elements cells = imagesSection.get(i).select("td");
      String type = cells.get(0).text();

      // 图片部分
      Elements images = cells.get(1).select("a > img");
      for (int j = 0; j < images.size(); j++) {
        imagesMeta.add(new OneNNNImageMetadata(idx++, type, j + 1,
            addPrefix(images.get(j).attr("src")), "", "", "", "", ""));
      }
    }

    return imagesMeta;
  }

  /**
   * 添加站点URL作为前缀。
   * 
   * @param imageHref
   * @return
   */
  private String addPrefix(String imageHref) {
    if (imageHref.startsWith("/")) {
      imageHref = TMP_ITEM_BASE + imageHref;
    }

    return imageHref;
  }

  private static class OneNNNCrawlConstant {

    // 选择器定义
    // 商品名称
    public static final String SEL_TITLE = "#masterBody_cellHeaderTitle strong";
    // 封绘小图
    public static final String SEL_COVER = "#masterBody_imgMain";
    // 制造商，比例，发售日，商品代码
    public static final String SEL_META_1 = "#tblItemInfo > tbody > tr";
    // 商品系列，国家
    public static final String SEL_META_2 = "table.tag01 tr";
    // 图片区域
    public static final String SEL_IMAGES = "#masterBody_pnlImgCa tr";
    // 图片页
    public static final String SEL_LARGE_IMAGES = "#imgAll img";

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
