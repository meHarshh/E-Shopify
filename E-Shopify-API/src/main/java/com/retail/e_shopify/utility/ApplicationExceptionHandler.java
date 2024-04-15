package com.retail.e_shopify.utility;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.retail.e_shopify.exceptions.UserAlreadyExistByEmailException;

public class ApplicationExceptionHandler {
	private ErrorStructure<String> errorStructure;

	private ResponseEntity<ErrorStructure<String>> erroResponse(HttpStatus status, String message, String rootCause) {
		return new ResponseEntity<ErrorStructure<String>>(
				errorStructure.setStatus(status.value()).setMessage(message).setRootCause(rootCause), status);
	}

	private ResponseEntity<ErrorStructure<String>> handleUserAlreadyExistByEmail(UserAlreadyExistByEmailException ex) {
		return erroResponse(HttpStatus.BAD_REQUEST, ex.getMessage(),
				"The email you are trying to register is already registered please try with a new email");
	}

}
