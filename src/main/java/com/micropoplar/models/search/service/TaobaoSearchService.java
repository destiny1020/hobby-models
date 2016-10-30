package com.micropoplar.models.search.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.micropoplar.models.search.dto.req.ReqSearchParam;
import com.micropoplar.models.search.dto.resp.RespSearchResult;
import com.micropoplar.models.search.dto.resp.RespSearchResultWrapper;

/**
 * 淘宝搜索相关服务。
 * 
 * @author ruixiang
 *
 */
@Service
public class TaobaoSearchService {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private static final String TMP_SEARCH = "https://s.taobao.com/search?q=%s&s=%d&sort=price-asc";

  @Autowired
  private ObjectMapper mapper;

  @SuppressWarnings("unchecked")
  public RespSearchResultWrapper search(ReqSearchParam reqSearch)
      throws MalformedURLException, IOException {
    // 处理搜索请求体
    reqSearch.setKeyword(processSearchKeywords(reqSearch.getKeyword()));
    reqSearch.fix();

    logger.info("[搜索]关键字: " + reqSearch.getKeyword());

    Document doc = Jsoup.parse(
        new URL(String.format(TMP_SEARCH, reqSearch.getKeyword(), reqSearch.getOffset())), 20000);
    String docString = doc.toString();

    Pattern pattern = Pattern.compile("g_page_config = ([\\S ]*);");
    Matcher matcher = pattern.matcher(docString);

    if (!matcher.find()) {
      return new RespSearchResultWrapper(reqSearch, 0, Collections.EMPTY_LIST);
    }

    String object = matcher.group(1);
    // System.out.println(object);
    Map<String, Object> readValue = mapper.readValue(object, Map.class);
    List<Map<String, Object>> items =
        (List<Map<String, Object>>) (((Map<String, Object>) ((Map<String, Object>) ((Map<String, Object>) readValue
            .get("mods")).get("itemlist")).get("data"))).get("auctions");

    if (reqSearch.getSize() != 44) {
      items = items.subList(0, reqSearch.getSize());
    }

    List<RespSearchResult> results =
        items.stream().map(RespSearchResult::new).collect(Collectors.toList());

    return new RespSearchResultWrapper(reqSearch, results.size(), results);
  }

  private String processSearchKeywords(String keyword) {
    keyword = keyword.trim();
    return keyword.replaceAll("\\s+", "+");
  }

}
