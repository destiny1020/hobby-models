package com.micropoplar.models.search.exception;

import com.micropoplar.models.common.response.ApiError;

/**
 * 和搜索功能相关的异常。
 * 
 * @author ruixiang
 *
 */
public class ModelsSearchExceptions {

  public static final ApiError EX_SEARCH_NO_KEYWORD = new ApiError(90001, "请提供搜索关键字");

}
