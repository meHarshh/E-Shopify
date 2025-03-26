package com.retail.e_shopify.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@SuppressWarnings("serial")
@Getter
@AllArgsConstructor
public class OtpExpiredException extends RuntimeException {

	private String message;
}
