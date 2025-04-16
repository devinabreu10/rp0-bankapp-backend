package dev.abreu.bankapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import dev.abreu.bankapp.utils.ResourceType;

import java.io.Serial;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

	/**
	 * throws exception if a resource being accessed through a request is not found
	 * 
	 */
	@Serial
	private static final long serialVersionUID = 9177986844664132597L;
	
	public ResourceNotFoundException(ResourceType resourceType, Object identifier) {
		super(resourceType.getResourceName() + " not found with identifier: " + identifier);
	}

	public ResourceNotFoundException(String message) {
		super(message);
	}

}
