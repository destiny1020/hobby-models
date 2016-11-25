package com.micropoplar.models.crawl.spec;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.micropoplar.models.crawl.biz.OneNNNCrawlListPage;
import com.micropoplar.models.crawl.domain.OneNNNRecordListRaw;

/**
 * 列表抓取服务单元测试。
 * 
 * @author ruixiang
 *
 */
public class OneNNNCrawlListPageSpec {

  private static Document firstPageDoc;
  private static Document secondPageDoc;
  private static Document lastPageDoc;
  private static OneNNNCrawlListPage firstPage;
  private static OneNNNCrawlListPage secondPage;
  private static OneNNNCrawlListPage lastPage;

  @Rule
  public ExpectedException exception = ExpectedException.none();

  @BeforeClass
  public static void beforeClass() throws IOException {
    firstPageDoc = Jsoup.parse(new File("testdata/OneNNNCrawlListPage/list-first.html"), "UTF-8");
    secondPageDoc = Jsoup.parse(new File("testdata/OneNNNCrawlListPage/list-second.html"), "UTF-8");
    lastPageDoc = Jsoup.parse(new File("testdata/OneNNNCrawlListPage/list-last.html"), "UTF-8");
    firstPage = spy(new OneNNNCrawlListPage("タミヤ", 1));
    secondPage = spy(new OneNNNCrawlListPage("タミヤ", 2));
    lastPage = spy(new OneNNNCrawlListPage("タミヤ", 28));

    doReturn(firstPageDoc).when(firstPage).getDoc();
    doReturn(secondPageDoc).when(secondPage).getDoc();
    doReturn(lastPageDoc).when(lastPage).getDoc();
    doReturn(secondPage).when(firstPage).navToNext();
  }

  @Test
  public void whenLoadFirstPageThenHas40ListItems() {
    int count = firstPage.getItemsCount();
    assertThat(count, is(40));
  }

  @Test
  public void whenLoadFirstPageThenHasNextPage() {
    boolean hasNext = firstPage.hasNextPage();
    assertThat(hasNext, is(Boolean.TRUE));
  }

  @Test
  public void whenLoadLastPageThenHas23ListItems() {
    int count = lastPage.getItemsCount();
    assertThat(count, is(23));
  }

  @Test
  public void whenLoadLastPageThenDoesNotHasNextPage() {
    boolean hasNext = lastPage.hasNextPage();
    assertThat(hasNext, is(Boolean.FALSE));
  }

  @Test
  public void whenParseFirstPageThenVerifyFirstItem() {
    List<OneNNNRecordListRaw> items = firstPage.parseItems();
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

  @Test
  public void whenNavToSecondPageThenHas40ListItems() {
    OneNNNCrawlListPage secondPage = firstPage.navToNext();
    int count = secondPage.getItemsCount();
    assertThat(count, is(40));
  }

  @Test
  public void whenNavToSecondPageThenVerifyLastItem() {
    OneNNNCrawlListPage secondPage = firstPage.navToNext();
    List<OneNNNRecordListRaw> items = secondPage.parseItems();
    assertThat(items.size(), is(40));

    OneNNNRecordListRaw firstRecord = items.get(items.size() - 1);
    assertThat(firstRecord.getCoverUrl(), is("http://www.1999.co.jp/itsmall26/10265219s.jpg"));
    assertThat(firstRecord.getTitle(), is("ロッキード マーチン F-16CJ [ブロック50] ファイティングファルコン (プラモデル)"));
    assertThat(firstRecord.getSn(), is("10265219"));
    assertThat(firstRecord.getMakers(), is("タミヤ"));
    assertThat(firstRecord.getRelease(), is("2014年4月上旬"));
    assertThat(firstRecord.getScales(), is("1/72"));
    assertThat(firstRecord.getSeries(), is("ウォーバード、No.86"));
    assertThat(firstRecord.getCode(), is("60786"));
  }

  @Test
  public void whenInLastPageNavToNextThenREThrown() {
    exception.expect(RuntimeException.class);
    exception.expectMessage("没有下一页了");
    lastPage.navToNext();
  }

}
