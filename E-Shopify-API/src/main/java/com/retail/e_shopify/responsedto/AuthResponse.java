package com.retail.e_shopify.responsedto;

import com.retail.e_shopify.enums.UserRole;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthResponse {
	
	private int userId;
	private String userName;
	private UserRole role;
	private long accessExpiration;
	private long refreshExpiration;
}
