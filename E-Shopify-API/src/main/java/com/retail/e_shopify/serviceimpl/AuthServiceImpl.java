package com.retail.e_shopify.serviceimpl;

import java.util.Random;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.retail.e_shopify.cache.CacheStore;
import com.retail.e_shopify.entity.Customer;
import com.retail.e_shopify.entity.Seller;
import com.retail.e_shopify.entity.User;
import com.retail.e_shopify.enums.UserRole;
import com.retail.e_shopify.exceptions.InvalidRoleException;
import com.retail.e_shopify.exceptions.UserAlreadyExistByEmailException;
import com.retail.e_shopify.repository.UserRepository;
import com.retail.e_shopify.requestdto.UserRequest;
import com.retail.e_shopify.responsedto.UserResponse;
import com.retail.e_shopify.service.AuthService;
import com.retail.e_shopify.utility.ResponseStructure;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

	private UserRepository userRepository;
	private ResponseStructure<UserResponse> responseStructure;
	private CacheStore<String> otpCache;
	private CacheStore<User> userCache;
	
	@Override
	public ResponseEntity<String> registerUser(UserRequest userRequest) {
		if (userRepository.existsByEmail(userRequest.getEmail()))
			throw new UserAlreadyExistByEmailException("Registration failed");
		User user = mapToChildEntity(userRequest);
		String otp = generateOTP();
		
		otpCache.add("otp", otp);
		userCache.add("user", user);
		
		return ResponseEntity.ok(otp);
	}

	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> verifyOtp(String otp) {
		if(!otpCache.get("otp").equals(otp))throw new RuntimeException();
		
		User user = userCache.get("user");
		user.setEmailVerified(true);
//		user = userRepository.save(user);
		
		return ResponseEntity.ok(responseStructure.setMessage("user Registeres as "+user.getUserRole())
				.setStatus(HttpStatus.OK.value())
				.setData(mapToUserResponse(user)));
		
	}

	private String generateOTP() {
		return String.valueOf(new Random().nextInt(100000, 999999));
	}

	private <T extends User> T mapToChildEntity(UserRequest userRequest) {
		UserRole role = userRequest.getUserRole();
		User user = null;
		switch (role) {
		case SELLER -> user = new Seller();
		case CUSTOMER -> user = new Customer();
		default -> throw new InvalidRoleException("Invalid role");
		}
		user.setUserName(userRequest.getEmail().split("@gmail.com")[0]);
		user.setPassword(userRequest.getPassword());
		user.setUserRole(userRequest.getUserRole());
		user.setEmail(userRequest.getEmail());
		user.setDisplayName(userRequest.getDisplayName());

		return (T) user;
	}

	private UserResponse mapToUserResponse(User user) {
		return UserResponse.builder().email(user.getEmail()).userName(user.getUserName()).userRole(user.getUserRole())
				.userName(user.getUserName()).displayName(user.getDisplayName()).isEmailVerified(user.isEmailVerified())
				.isDeleted(user.isDeleted()).build();
	}


}
