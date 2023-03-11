package com.picture.publishing.publisher.controller.exception;

public class NotAllowedRequestException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public NotAllowedRequestException(String message) {
    super(message);
  }
}
