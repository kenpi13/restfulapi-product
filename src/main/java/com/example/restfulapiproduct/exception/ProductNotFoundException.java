package com.example.restfulapiproduct.exception;

/** ProductNotFoundException */
public class ProductNotFoundException extends RuntimeException {
  private static final long serialVersionUID = 9042961851559576742L;

  public ProductNotFoundException(String msg) {
    super(msg);
  }
}
