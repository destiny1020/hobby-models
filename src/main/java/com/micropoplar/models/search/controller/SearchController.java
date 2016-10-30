package com.micropoplar.models.search.controller;

import java.io.IOException;
import java.net.MalformedURLException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.micropoplar.models.common.response.ApiResponse;
import com.micropoplar.models.common.service.ResponseGenerator;
import com.micropoplar.models.search.dto.req.ReqSearchParam;
import com.micropoplar.models.search.exception.ModelsSearchExceptions;
import com.micropoplar.models.search.service.TaobaoSearchService;

@RestController
@RequestMapping("/api/public/search")
public class SearchController {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private ResponseGenerator resGen;

  @Autowired
  private TaobaoSearchService searchService;

  @RequestMapping(method = RequestMethod.GET)
  public ResponseEntity<ApiResponse> index(ReqSearchParam reqSearch)
      throws MalformedURLException, IOException {
    if (StringUtils.isBlank(reqSearch.getKeyword())) {
      return resGen.errorResponse(ModelsSearchExceptions.EX_SEARCH_NO_KEYWORD);
    }

    return resGen.response(searchService.search(reqSearch));
  }


}
