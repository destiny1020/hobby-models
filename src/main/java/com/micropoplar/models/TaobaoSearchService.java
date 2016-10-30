package com.micropoplar.models;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.micropoplar.models.search.dto.RespSearchResult;

/**
 * 淘宝搜索相关服务。
 * 
 * @author ruixiang
 *
 */
@Service
public class TaobaoSearchService {

  private static final String TMP_SEARCH = "https://s.taobao.com/search?q=%s&sort=price-asc";
  private static final ObjectMapper mapper = new ObjectMapper();

  @SuppressWarnings("unchecked")
  public List<RespSearchResult> search(String keyword) throws MalformedURLException, IOException {
    Document doc = Jsoup.parse(new URL(String.format(TMP_SEARCH, keyword)), 20000);
    String docString = doc.toString();
    // System.out.println(docString);
    // System.out.println(" --- --- --- --- ");

    Pattern pattern = Pattern.compile("g_page_config = ([\\S ]*);");
    Matcher matcher = pattern.matcher(docString);

    System.out.println(matcher.find());
    String object = matcher.group(1);
    // System.out.println(object);
    Map<String, Object> readValue = mapper.readValue(object, Map.class);
    List<Map<String, Object>> items =
        (List<Map<String, Object>>) (((Map<String, Object>) ((Map<String, Object>) ((Map<String, Object>) readValue
            .get("mods")).get("itemlist")).get("data"))).get("auctions");

    return items.stream().map(RespSearchResult::new).collect(Collectors.toList());
  }

  public static void main(String[] args) throws MalformedURLException, IOException {
    TaobaoSearchService service = new TaobaoSearchService();
    List<RespSearchResult> results = service.search("威龙+6600");
    // List<RespSearchResult> results = service.search("田宫+35250");
    System.out.println("Size: " + results.size());
    results.forEach(System.out::println);
  }

}
