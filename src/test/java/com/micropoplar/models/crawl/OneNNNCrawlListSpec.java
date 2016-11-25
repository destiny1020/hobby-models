package com.micropoplar.models.crawl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.BeforeClass;
import org.junit.Test;

import com.micropoplar.models.crawl.domain.OneNNNRecordListRaw;
import com.micropoplar.models.crawl.service.OneNNNCrawlListService;

/**
 * 列表抓取服务单元测试。
 * 
 * @author ruixiang
 *
 */
public class OneNNNCrawlListSpec {

  private static Document firstPage;
  private static Document lastPage;
  private static OneNNNCrawlListService service;

  @BeforeClass
  public static void beforeClass() throws IOException {
    firstPage = Jsoup.parse(new File("testdata/OneNNNCrawlListService/list-first.html"), "UTF-8");
    lastPage = Jsoup.parse(new File("testdata/OneNNNCrawlListService/list-last.html"), "UTF-8");
    service = new OneNNNCrawlListService();
  }

  @Test
  public void whenLoadFirstPageThenHas40ListItems() {
    int count = service.getItemsCount(firstPage);
    assertThat(count, is(40));
  }

  @Test
  public void whenLoadFirstPageThenHasNextPage() {
    boolean hasNext = service.hasNextPage(firstPage);
    assertThat(hasNext, is(Boolean.TRUE));
  }

  @Test
  public void whenLoadLastPageThenHas23ListItems() {
    int count = service.getItemsCount(lastPage);
    assertThat(count, is(23));
  }

  @Test
  public void whenLoadLastPageThenDoesNotHasNextPage() {
    boolean hasNext = service.hasNextPage(lastPage);
    assertThat(hasNext, is(Boolean.FALSE));
  }

  @Test
  public void whenParseFirstPageThenVerifyFirstItem() {
    List<OneNNNRecordListRaw> items = service.parseItems(firstPage);
    assertThat(items.size(), is(40));

    OneNNNRecordListRaw firstRecord = items.get(0);
    assertThat(firstRecord.getCoverUrl(), is("http://www.1999.co.jp/itsmall42/10428884s.jpg"));
    assertThat(firstRecord.getTitle(), is("日本陸軍四式戦闘機 疾風 & くろがね四起 情景セット (プラモデル)"));
    assertThat(firstRecord.getSn(), is("10428884"));
    assertThat(firstRecord.getMakers(), is("タミヤ"));
    assertThat(firstRecord.getRelease(), is("12月下旬(2016/11/9予約開始)"));
    assertThat(firstRecord.getScales(), is("1/48"));
    assertThat(firstRecord.getSeries(), is("1/48 傑作機、No.116"));
    assertThat(firstRecord.getCode(), is("61116"));
  }

}
