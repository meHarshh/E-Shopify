package com.retail.e_shopify.utility;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.retail.e_shopify.exceptions.RegistrationSessionExpiredException;
import com.retail.e_shopify.exceptions.UserAlreadyExistByEmailException;

public class ApplicationExceptionHandler {
	private ErrorStructure<String> errorStructure;

	private ResponseEntity<ErrorStructure<String>> erroResponse(HttpStatus status, String message, String rootCause) {
		return new ResponseEntity<ErrorStructure<String>>(
				errorStructure.setStatus(status.value()).setMessage(message).setRootCause(rootCause), status);
	}

	@ExceptionHandler
	@SuppressWarnings("unused")
	private ResponseEntity<ErrorStructure<String>> handleUserAlreadyExistByEmail(UserAlreadyExistByEmailException ex) {
		return erroResponse(HttpStatus.BAD_REQUEST, ex.getMessage(),
				"The email you are trying to register is already registered please try with a new email");
	}
	
	@ExceptionHandler
	@SuppressWarnings("unused")
	private ResponseEntity<ErrorStructure<String>> handleUserRegistrationSessionExpired(RegistrationSessionExpiredException ex) {
		return erroResponse(HttpStatus.BAD_REQUEST, ex.getMessage(),
				"Registration time expired as you took more then the wait time");
	}

}
