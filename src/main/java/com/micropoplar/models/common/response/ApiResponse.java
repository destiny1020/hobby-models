package com.micropoplar.models.common.response;

public class ApiResponse {

  /**
   * API调用状态
   */
  private final ApiStatus status;

  /**
   * API返回数据
   */
  private final Object payload;

  /**
   * API调用异常
   */
  private final ApiError error;

  /**
   * 正常情况下的调用
   * 
   * @param status
   * @param token
   * @param payload
   */
  public ApiResponse(Object payload) {
    this(ApiStatus.OK, payload, null);
  }

  /**
   * 异常情况下的调用
   * 
   * @param error
   */
  public ApiResponse(ApiError error) {
    this(ApiStatus.ERROR, null, error);
  }

  public ApiResponse(ApiStatus status, Object payload, ApiError error) {
    super();
    this.status = status;
    this.payload = payload;
    this.error = error;
  }

  public ApiStatus getStatus() {
    return status;
  }

  public Object getPayload() {
    return payload;
  }

  public ApiError getError() {
    return error;
  }

}
