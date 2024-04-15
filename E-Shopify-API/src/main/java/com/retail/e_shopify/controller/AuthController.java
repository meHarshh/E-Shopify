package com.retail.e_shopify.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.retail.e_shopify.requestdto.UserRequest;
import com.retail.e_shopify.responsedto.UserResponse;
import com.retail.e_shopify.service.AuthService;
import com.retail.e_shopify.utility.ResponseStructure;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class AuthController {
	
	private AuthService authService;
	
	@PostMapping(value = "/users/registerUser")
	public ResponseEntity<String> registerUser(@RequestBody UserRequest userRequest){
		return authService.registerUser(userRequest);
	}
	
	@PostMapping(value = "/verify/")
	public ResponseEntity<ResponseStructure<UserResponse>> verifyOtp(@RequestParam String otp){
		return authService.verifyOtp(otp);
	}
	
	
}
