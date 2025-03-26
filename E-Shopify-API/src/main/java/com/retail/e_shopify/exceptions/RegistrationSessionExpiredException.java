package com.retail.e_shopify.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@SuppressWarnings("serial")
@Getter
@AllArgsConstructor
public class RegistrationSessionExpiredException extends RuntimeException {
	private String message;
}
