package com.example.restfulapiproduct.exception;

/** BadRequestException */
public class BadRequestException extends RuntimeException {
  private static final long serialVersionUID = -7096742011858305697L;

  public BadRequestException(String msg) {
    super(msg);
  }
}
