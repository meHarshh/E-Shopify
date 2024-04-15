package com.retail.e_shopify.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@SuppressWarnings("serial")
@AllArgsConstructor
@Getter
public class UserAlreadyExistByEmailException extends RuntimeException {

	private String message;
}
