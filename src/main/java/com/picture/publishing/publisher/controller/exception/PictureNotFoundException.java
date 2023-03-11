package com.picture.publishing.publisher.controller.exception;

public class PictureNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public PictureNotFoundException(Long id) {
    super("Picture with id " + id + " not found");
  }

}
