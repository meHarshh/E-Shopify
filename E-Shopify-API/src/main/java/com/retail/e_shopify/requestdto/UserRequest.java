package com.retail.e_shopify.requestdto;

import com.retail.e_shopify.enums.UserRole;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserRequest {

	private String displayName;
	private String email;
	private String password;
	private UserRole userRole;

}
