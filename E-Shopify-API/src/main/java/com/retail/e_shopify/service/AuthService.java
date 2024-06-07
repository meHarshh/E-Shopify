package com.retail.e_shopify.service;

import org.springframework.http.ResponseEntity;

import com.retail.e_shopify.requestdto.AuthRequest;
import com.retail.e_shopify.requestdto.OtpRequest;
import com.retail.e_shopify.requestdto.UserRequest;
import com.retail.e_shopify.responsedto.AuthResponse;
import com.retail.e_shopify.responsedto.UserResponse;
import com.retail.e_shopify.utility.ResponseStructure;
import com.retail.e_shopify.utility.SimpleResponseStructure;

import jakarta.mail.MessagingException;

public interface AuthService {

	ResponseEntity<SimpleResponseStructure> registerUser(UserRequest userRequest) throws MessagingException;

	ResponseEntity<ResponseStructure<UserResponse>> verifyOtp(OtpRequest otpRequest);

	ResponseEntity<ResponseStructure<AuthResponse>> login(AuthRequest authRequest);

}
