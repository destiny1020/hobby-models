package com.micropoplar.models.crawl.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
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

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.micropoplar.infra.imagesdk.service.IImageManager;
import com.micropoplar.infra.imagesdk.service.IResponse;
import com.micropoplar.models.crawl.constant.CrawlContant;
import com.micropoplar.models.crawl.domain.OneNNNRecordImage;
import com.micropoplar.models.crawl.domain.OneNNNRecordRaw;
import com.micropoplar.models.crawl.service.biz.OneNNNImageMetadata;
import com.micropoplar.models.crawl.util.CrawlConnectionUtil;
import com.micropoplar.models.crawl.util.OneNNNCrawlerUtil;

/**
 * 1999抓取服务。
 * 
 * @author ruixiang
 *
 */
@Service
@Transactional
public class OneNNNCrawlService {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private static final String TMP_ITEM_URL = CrawlContant.CRAWL_1999_SITE_BASE + "/%s/";
  private static final String TMP_ITEM_IMAGES_URL =
      CrawlContant.CRAWL_1999_SITE_BASE + "/image/%s/10/0";

  private static final int RETRY_MAX = 5;

  @Autowired
  private EntityManager em;

  @Autowired
  private IImageManager imageManager;

  /**
   * 判断当前爬取记录是否和历史爬取记录相等。
   * 
   * @param nowRecord
   * @param imagesMeta
   * @return
   */
  public Pair<Boolean, Boolean> isTheSameWithLatestItem(OneNNNRecordRaw nowRecord,
      List<OneNNNImageMetadata> imagesMeta) {
    try {
      OneNNNRecordRaw latestRecord =
          em.createNamedQuery(OneNNNRecordRaw.FIND_BY_SN, OneNNNRecordRaw.class)
              .setParameter(1, nowRecord.getSn()).setMaxResults(1).getSingleResult();

      boolean fieldsEqual = nowRecord.equals(latestRecord);
      boolean imagesCountEqual = imagesMeta.size() == latestRecord.getImages().size();

      if (!fieldsEqual) {
        logger.info(String.format("[爬虫 - 基本] SN: %s %s - 基本信息发生了改变", nowRecord.getSn(),
            nowRecord.getTitle()));
      }
      if (!imagesCountEqual) {
        logger.info(String.format("[爬虫 - 基本] SN: %s %s - 图片数量发生了改变", nowRecord.getSn(),
            nowRecord.getTitle()));
      }

      return Pair.of(fieldsEqual, imagesCountEqual);
    } catch (NoResultException nre) {
      return Pair.of(Boolean.FALSE, Boolean.FALSE);
    }
  }

  /**
   * 爬取1999详情数据。
   * 
   * @param sn
   * @throws MalformedURLException
   * @throws IOException
   */
  public OneNNNRecordRaw crawl(String sn) throws MalformedURLException, IOException {
    logger.info("[爬虫]商品详情页: " + sn);

    Document doc = CrawlConnectionUtil.getDocument(String.format(TMP_ITEM_URL, sn));

    OneNNNRecordRaw record = new OneNNNRecordRaw();
    record.setSn(sn);

    // 商品名称
    Element titleElem = doc.select(OneNNNCrawlConstant.SEL_TITLE).first();
    String title = titleElem.text();
    record.setTitle(title);

    // 封绘小图
    Element coverElem = doc.select(OneNNNCrawlConstant.SEL_COVER).first();
    // 如果是带有确认的详情页面，放弃
    if (coverElem == null) {
      record.setShouldSave(Boolean.FALSE);
      return record;
    }

    String coverUrl = OneNNNCrawlerUtil.addPrefix(coverElem.attr("src"));
    record.setCoverUrl(coverUrl);

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
    List<String> releaseDates = meta1Map.get(OneNNNCrawlConstant.KEY_RELEASE_DATE);
    if (releaseDates != null && releaseDates.size() == 1) {
      record.setReleaseDateRaw(releaseDates.get(0));
      logger.info(String.format("[爬虫 - 基本] SN: %s, 发售日: %s", sn, releaseDates));
    }

    // 发售预定日
    List<String> releaseReserveDates = meta1Map.get(OneNNNCrawlConstant.KEY_RELEASE_RESERVE_DATE);
    if (releaseReserveDates != null && releaseReserveDates.size() == 1) {
      record.setReleaseReserveDateRaw(releaseReserveDates.get(0));
      logger.info(String.format("[爬虫 - 基本] SN: %s, 发售预定日: %s", sn, releaseReserveDates));
    }

    // 商品代码
    List<String> codes = meta1Map.get(OneNNNCrawlConstant.KEY_CODE);
    if (codes != null && codes.size() == 1) {
      record.setCode(codes.get(0));
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

    // 商品系列 - 区域1或者区域2
    List<String> series = meta1Map.get(OneNNNCrawlConstant.KEY_SERIES_1);
    if (series != null && series.size() > 0) {
      record.setSeries(series);
      logger.info(String.format("[爬虫 - 基本] SN: %s, 系列: %s", sn, series));
    }
    if (series == null || series.size() == 0) {
      series = meta2Map.get(OneNNNCrawlConstant.KEY_SERIES_2);
      if (series != null && series.size() > 0) {
        record.setSeries(series);
        logger.info(String.format("[爬虫 - 基本] SN: %s, 系列: %s", sn, series));
      }
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

    // 只有在爬取的信息和上次不同时，才会调用保存操作
    Pair<Boolean, Boolean> equalsInfo = this.isTheSameWithLatestItem(record, imagesMeta);
    if (!equalsInfo.getRight().booleanValue()) {
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

        // 转换成实体类型
        record.setImages(
            imagesMeta.stream().map(OneNNNRecordImage::new).collect(Collectors.toList()));
      });
    }

    if (!equalsInfo.getLeft().booleanValue() || !equalsInfo.getRight().booleanValue()) {
      record.setShouldSave(Boolean.TRUE);
    } else {
      logger.info(String.format("[爬虫 - 基本] SN: %s 已经被爬取了，且信息并没有发生变化，无需保存", sn));
    }

    return record;
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

      int downloadRetryTimes = 0;
      while (downloadRetryTimes < RETRY_MAX) {
        logger.info(String.format("[爬虫 - 图片] SN: %s 第%d次尝试下载%s图: %d - %s", sn,
            downloadRetryTimes + 1, isSmall ? "小" : "大", meta.getIdx(), targetUrl));
        FileOutputStream out = new FileOutputStream(localImageName);
        URLConnection conn = new URL(targetUrl).openConnection();
        conn.setConnectTimeout(10000);
        try {
          conn.connect();
          InputStream is = conn.getInputStream();
          imageBytes = IOUtils.toByteArray(IOUtils.toBufferedInputStream(is));
          out.write(imageBytes);
          out.flush();
          out.close();
          is.close();
          break;
        } catch (Exception e) {
          downloadRetryTimes++;
          if (downloadRetryTimes >= RETRY_MAX) {
            throw new RuntimeException(String.format("下载图片失败: %s - %s", sn, targetUrl));
          }
        }
      }
    } else {
      // 已经存在
      FileInputStream in = new FileInputStream(localImageName);
      imageBytes = IOUtils.toByteArray(IOUtils.toBufferedInputStream(in));
    }

    if (isSmall) {
      meta.setSmallLocalImageName(localImageName);
    } else {
      meta.setLargeLocalImageName(localImageName);
    }

    // 上传图片到七牛
    String imageUrl = "";

    int uploadRetryTimes = 0;
    while (uploadRetryTimes < RETRY_MAX) {
      try {
        logger.info(String.format("[爬虫 - 图片] SN: %s 第%d次尝试: 上传%s图到七牛: %d - %s", sn,
            uploadRetryTimes + 1, isSmall ? "小" : "大", meta.getIdx(), localImageName));
        imageUrl = uploadToQiniu(sn, imageBytes);
        break;
      } catch (Exception e) {
        uploadRetryTimes++;
        if (uploadRetryTimes >= RETRY_MAX) {
          throw new RuntimeException(String.format("上传图片失败: %s - %s", sn, localImageName));
        }
      }
    }

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

    Document doc = CrawlConnectionUtil.getDocument(String.format(TMP_ITEM_IMAGES_URL, sn));

    Elements largeImages = doc.select(OneNNNCrawlConstant.SEL_LARGE_IMAGES);
    assert imagesMeta.size() == largeImages.size();
    for (int i = 0; i < imagesMeta.size(); i++) {
      imagesMeta.get(i)
          .setLargeImageUrl(OneNNNCrawlerUtil.addPrefix(largeImages.get(i).attr("src")));
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
            OneNNNCrawlerUtil.addPrefix(images.get(j).attr("src")), "", "", "", "", ""));
      }
    }

    return imagesMeta;
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
    // 商品系列 - 区域1
    public static final String KEY_SERIES_1 = "シリーズ";
    // 商品系列 - 区域2
    public static final String KEY_SERIES_2 = "商品シリーズ";
    // 国家
    public static final String KEY_COUNTRY = "国";

  }

}
