package com.example.restfulapiproduct.exception;

/** ImageNotUploadException */
public class ImageNotUploadException extends RuntimeException {
  private static final long serialVersionUID = 1500068990771489303L;

  public ImageNotUploadException(String msg) {
    super(msg);
  }
}
