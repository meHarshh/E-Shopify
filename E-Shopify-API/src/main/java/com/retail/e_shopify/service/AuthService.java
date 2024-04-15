package com.retail.e_shopify.service;

import org.springframework.http.ResponseEntity;

import com.retail.e_shopify.requestdto.UserRequest;
import com.retail.e_shopify.responsedto.UserResponse;
import com.retail.e_shopify.utility.ResponseStructure;

public interface AuthService {

	ResponseEntity<String> registerUser(UserRequest userRequest);

	ResponseEntity<ResponseStructure<UserResponse>> verifyOtp(String otp);

}
