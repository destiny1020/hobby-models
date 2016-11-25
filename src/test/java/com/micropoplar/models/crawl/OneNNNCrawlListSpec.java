package com.micropoplar.models.crawl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.io.File;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.BeforeClass;
import org.junit.Test;

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

}
