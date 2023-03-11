package com.picture.publishing.publisher.controller.exception;

import java.util.Date;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	private final Logger errorLogger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler({ NotAllowedRequestException.class, Exception.class })
	public ResponseEntity<ErrorDetails> handleNotAllowedRequestException(NotAllowedRequestException e,
			WebRequest request) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), e.getMessage(), request.getDescription(false));
		errorLogger.error("Bad Request Exception: {}", e.getMessage());

		return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler({ PictureNotFoundException.class, EntityNotFoundException.class })
	public ResponseEntity<ErrorDetails> handleNotFoundException(Exception e, WebRequest request) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), e.getMessage(), request.getDescription(false));
		errorLogger.error("Not Found Exception: {}", e.getMessage());
		return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(EntityExistsException.class)
	public ResponseEntity<ErrorDetails> handleEntityExistsException(EntityExistsException e, WebRequest request) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), e.getMessage(), request.getDescription(false));
		errorLogger.error("Expectation Failed Exception: {}", e.getMessage());

		return new ResponseEntity<>(errorDetails, HttpStatus.EXPECTATION_FAILED);
	}

	@ExceptionHandler(EntityAlreadyExistsException.class)
	public ResponseEntity<ErrorDetails> handleEntityAlreadyExistsException(EntityAlreadyExistsException e,
			WebRequest request) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), e.getMessage(), request.getDescription(false));
		errorLogger.error("Conflict Exception: {}", e.getMessage());

		return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public ResponseEntity<ErrorDetails> handleMaxSizeException(MaxUploadSizeExceededException e, WebRequest request) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), e.getMessage(), request.getDescription(false));
		errorLogger.error("Max Size file uploaded exceeded Exception: {}", e.getMessage());

		return new ResponseEntity<>(errorDetails, HttpStatus.EXPECTATION_FAILED);
	}

}
