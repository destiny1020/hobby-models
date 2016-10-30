package com.micropoplar.models.common.response;

public class ApiError {

  private final int code;
  private final String message;
  private Object data;

  public ApiError(int code, String message) {
    super();
    this.code = code;
    this.message = message;
  }

  public ApiError(int code, String message, Object data) {
    this(code, message);
    this.data = data;
  }

  public int getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }

  public Object getData() {
    return data;
  }

}
