package com.example.restfulapiproduct.dto;

import lombok.Data;

import java.util.Collections;
import java.util.List;

/** エラーレスポンスDto */
@Data
public class ErrorResponseDto {
  private String message;
  private List<String> errors;

  public ErrorResponseDto(String message, List<String> errors) {
    this.message = message;
    this.errors = errors;
  }

  public ErrorResponseDto(String message, String error) {
    this.message = message;
    errors = Collections.singletonList(error);
  }
}
