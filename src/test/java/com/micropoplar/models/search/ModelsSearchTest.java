package com.micropoplar.models.search;

import java.io.IOException;
import java.net.MalformedURLException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.micropoplar.models.search.dto.req.ReqSearchParam;
import com.micropoplar.models.search.dto.resp.RespSearchResultWrapper;
import com.micropoplar.models.search.service.TaobaoSearchService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ModelsSearchTest {

  @Autowired
  private TaobaoSearchService searchService;

  @Test
  public void testModelSearch() throws MalformedURLException, IOException {
    ReqSearchParam param = new ReqSearchParam();
    param.setKeyword("  威龙   虎式    ");

    RespSearchResultWrapper results = searchService.search(param);
    // List<RespSearchResult> results = service.search("田宫+35250");
    System.out.println("Size: " + results.getResults().size());
    results.getResults().forEach(System.out::println);
  }

}
