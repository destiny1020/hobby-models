package com.micropoplar.models.infra.exception;

import com.micropoplar.models.common.response.ApiError;

/**
 * 定义通用错误类型。
 * 
 * @author ruixiang
 *
 */
public class CommonApiExceptions {

  public static final ApiError ERROR_MODELS_COMMON_UPLOAD_PIC_BAD_REQUEST =
      new ApiError(9011, "图片上传请求不正确");;

}
