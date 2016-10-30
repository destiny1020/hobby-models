package com.micropoplar.models.common.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import com.micropoplar.models.common.response.ApiError;
import com.micropoplar.models.common.response.ApiResponse;

@Component
public class ResponseGenerator {

  public ResponseEntity<ApiResponse> response(Object payload) {
    return new ResponseEntity<ApiResponse>(new ApiResponse(payload), null, HttpStatus.OK);
  }

  public ResponseEntity<ApiResponse> response(Object payload, HttpStatus status) {
    return new ResponseEntity<ApiResponse>(new ApiResponse(payload), null, status);
  }

  public ResponseEntity<ApiResponse> responseWithHeaders(Object payload,
      MultiValueMap<String, String> headers) {
    return new ResponseEntity<ApiResponse>(new ApiResponse(payload), headers, HttpStatus.OK);
  }

  public ResponseEntity<ApiResponse> errorResponse(ApiError error) {
    return errorResponse(error, HttpStatus.OK);
  }

  public ResponseEntity<ApiResponse> errorResponse(ApiError error, HttpStatus status) {
    return new ResponseEntity<ApiResponse>(new ApiResponse(error), null, status);
  }

}
