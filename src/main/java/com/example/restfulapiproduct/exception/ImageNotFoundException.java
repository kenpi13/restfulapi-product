package com.example.restfulapiproduct.exception;

/** ImageNotFoundException */
public class ImageNotFoundException extends RuntimeException {
  private static final long serialVersionUID = 1240955592469705122L;

  public ImageNotFoundException(String msg) {
    super(msg);
  }
}
