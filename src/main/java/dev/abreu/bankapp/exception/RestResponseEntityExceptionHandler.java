package dev.abreu.bankapp.exception;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
	
	private static final Logger log = LogManager.getLogger(RestResponseEntityExceptionHandler.class);
	
	@ExceptionHandler(value = { ResourceNotFoundException.class })
	protected ResponseEntity<Object> handleNotFound(RuntimeException ex, WebRequest request) {
		
		log.error("ResourceNotFoundException: {}", ex.getMessage());
		
		String bodyOfResponse = ex.getMessage();
		
		return handleExceptionInternal(ex, bodyOfResponse, 
				new HttpHeaders(), HttpStatus.NOT_FOUND, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, 
			HttpStatusCode status, WebRequest request) {
		
		log.error("MethodArgumentNotValidException: Validation error occurred", ex);
		
		List<String> errors = new ArrayList<>();
		
		for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.add(fieldError.getDefaultMessage());
        }
        for (ObjectError objectError : ex.getBindingResult().getGlobalErrors()) {
            errors.add(objectError.getDefaultMessage());
        }
		
		return handleExceptionInternal(ex, errors, 
				headers, HttpStatus.BAD_REQUEST, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleTypeMismatch(
			TypeMismatchException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		
		log.error("TypeMismatchException: Type mismatch error occurred: \n\t\t{}", ex.getMessage());

		Object[] args = {ex.getPropertyName(), ex.getValue()};
		String defaultDetail = "Type mismatch observed";
		ProblemDetail body = createProblemDetail(ex, status, defaultDetail, null, args, request);

		return handleExceptionInternal(ex, body, 
				headers, status, request);
	}

}
