package com.retail.e_shopify.requestdto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OtpRequest {
	private String email;
	private String otp;
}
