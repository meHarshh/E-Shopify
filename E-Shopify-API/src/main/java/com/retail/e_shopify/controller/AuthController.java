package com.retail.e_shopify.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.retail.e_shopify.entity.User;
import com.retail.e_shopify.jwt.JwtService;
import com.retail.e_shopify.requestdto.AuthRequest;
import com.retail.e_shopify.requestdto.OtpRequest;
import com.retail.e_shopify.requestdto.UserRequest;
import com.retail.e_shopify.responsedto.AuthResponse;
import com.retail.e_shopify.responsedto.UserResponse;
import com.retail.e_shopify.service.AuthService;
import com.retail.e_shopify.utility.ResponseStructure;
import com.retail.e_shopify.utility.SimpleResponseStructure;

import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@AllArgsConstructor
public class AuthController {
	
	private AuthService authService;
	private JwtService  jwtService;
	
	@PostMapping(value = "/users/registerUser")
	public ResponseEntity<SimpleResponseStructure> registerUser(@RequestBody UserRequest userRequest) throws MessagingException{
		return authService.registerUser(userRequest);
	}
	
	@PostMapping(value = "/verify")
	public ResponseEntity<ResponseStructure<UserResponse>> verifyOtp(@RequestBody OtpRequest otpRequest){
		return authService.verifyOtp(otpRequest);
	}
	
	@GetMapping(value = "/test")
	public String getRefreshToken() {
		return jwtService.generateAccessToken(new User());
	}

	@PostMapping(value = "/login")
	public ResponseEntity<ResponseStructure<AuthResponse>> login(@RequestBody AuthRequest authRequest){
		return authService.login(authRequest);
	}
	
	
}
