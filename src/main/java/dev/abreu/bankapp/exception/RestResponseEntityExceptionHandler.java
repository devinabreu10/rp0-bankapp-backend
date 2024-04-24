package dev.abreu.bankapp.exception;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.BadCredentialsException;
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
	protected ResponseEntity<Object> handleResourceNotFound(RuntimeException ex, WebRequest request) {
		
		log.error("ResourceNotFoundException: {}", ex.getMessage());
		
		String bodyOfResponse = ex.getMessage();
		
		return handleExceptionInternal(ex, bodyOfResponse, 
				new HttpHeaders(), HttpStatus.NOT_FOUND, request);
	}
	
	@ExceptionHandler(value = { UsernameTakenException.class })
	protected ResponseEntity<Object> handleUsernameTaken(UsernameTakenException ex, WebRequest request) {
		
		log.error("UsernameTakenException: {}", ex.getMessage());
		
		String bodyOfResponse = ex.getMessage();
		
		return handleExceptionInternal(ex, bodyOfResponse, 
				new HttpHeaders(), HttpStatus.CONFLICT, request);
	}
	
	@ExceptionHandler(value = { BadCredentialsException.class })
	protected ResponseEntity<Object> handleBadCredentials(BadCredentialsException ex, WebRequest request) {
		
		log.error("BadCredentialsException: Invalid username or password");
		
		String bodyOfResponse = ex.getMessage() + ": Invalid username or password";
		
		return handleExceptionInternal(ex, bodyOfResponse, 
				new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
	}
	
	@ExceptionHandler(value = { InsufficientFundsException.class })
	protected ResponseEntity<Object> handleInsufficientFundsException(InsufficientFundsException ex, WebRequest request) {
		
		log.error("InsufficientFundsException: {}", ex.getMessage());
		
		String bodyOfResponse = ex.getMessage();
		
		return handleExceptionInternal(ex, bodyOfResponse, 
				new HttpHeaders(), HttpStatus.CONFLICT, request);
	}
	
	@ExceptionHandler(value = { NoSuchElementException.class })
	protected ResponseEntity<Object> handleNoSuchElement(NoSuchElementException ex, WebRequest request) {
		
		log.error("NoSuchElementException: {}", ex.getMessage());
		
		String bodyOfResponse = ex.getMessage();
		
		return handleExceptionInternal(ex, bodyOfResponse, 
				new HttpHeaders(), HttpStatus.NOT_FOUND, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(@NonNull MethodArgumentNotValidException ex, @NonNull HttpHeaders headers, 
			@NonNull HttpStatusCode status, @NonNull WebRequest request) {
		
		log.error("MethodArgumentNotValidException: Validation error occurred: \n\t\t{}", ex.getMessage());
		
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
		@NonNull TypeMismatchException ex, @NonNull HttpHeaders headers, @NonNull HttpStatusCode status, @NonNull WebRequest request) {
		
		log.error("TypeMismatchException: Type mismatch error occurred: \n\t\t{}", ex.getMessage());

		Object[] args = {ex.getPropertyName(), ex.getValue()};
		String defaultDetail = "Type mismatch observed";
		ProblemDetail body = createProblemDetail(ex, status, defaultDetail, null, args, request);

		return handleExceptionInternal(ex, body, 
				headers, status, request);
	}

}
