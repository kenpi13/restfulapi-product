package com.example.restfulapiproduct.exception;

/** ProductAlreadyExistException */
public class ProductAlreadyExistException extends RuntimeException {
  private static final long serialVersionUID = -2913103118013503021L;

  public ProductAlreadyExistException(String msg) {
    super(msg);
  }
}
