package com.retail.e_shopify.serviceimpl;

import java.time.Duration;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.retail.e_shopify.cache.CacheStore;
import com.retail.e_shopify.entity.AccessToken;
import com.retail.e_shopify.entity.Customer;
import com.retail.e_shopify.entity.RefreshToken;
import com.retail.e_shopify.entity.Seller;
import com.retail.e_shopify.entity.User;
import com.retail.e_shopify.enums.UserRole;
import com.retail.e_shopify.exceptions.InvalidCredentialException;
import com.retail.e_shopify.exceptions.InvalidOtpException;
import com.retail.e_shopify.exceptions.InvalidRoleException;
import com.retail.e_shopify.exceptions.OtpExpiredException;
import com.retail.e_shopify.exceptions.RegistrationSessionExpiredException;
import com.retail.e_shopify.exceptions.UserAlreadyExistByEmailException;
import com.retail.e_shopify.jwt.JwtService;
import com.retail.e_shopify.mailservice.MailService;
import com.retail.e_shopify.repository.AccessTokenRepository;
import com.retail.e_shopify.repository.RefreshTokenRepository;
import com.retail.e_shopify.repository.UserRepository;
import com.retail.e_shopify.requestdto.AuthRequest;
import com.retail.e_shopify.requestdto.OtpRequest;
import com.retail.e_shopify.requestdto.UserRequest;
import com.retail.e_shopify.responsedto.AuthResponse;
import com.retail.e_shopify.responsedto.UserResponse;
import com.retail.e_shopify.service.AuthService;
import com.retail.e_shopify.utility.MessageModel;
import com.retail.e_shopify.utility.ResponseStructure;
import com.retail.e_shopify.utility.SimpleResponseStructure;

import jakarta.mail.MessagingException;

@Service
public class AuthServiceImpl implements AuthService {

	private UserRepository userRepository;
	private ResponseStructure<UserResponse> responseStructure;
	private CacheStore<String> otpCache;
	private CacheStore<User> userCache;
	private SimpleResponseStructure simpleResponseStructure;
	private MailService mailService;
	private AuthenticationManager authenticationManager;
	private JwtService jwtService;
	private AccessTokenRepository accessTokenRepository;
	private RefreshTokenRepository refreshTokenRepository;
	@Value("${myapp.jwt.access.expiration}")
	private long accessExpiration;
	@Value("${myapp.jwt.refresh.expiration}")
	private long refeshExpiration;
	@Autowired
	private PasswordEncoder passwordEncoder;
	

	public AuthServiceImpl(UserRepository userRepository, ResponseStructure<UserResponse> responseStructure,
			CacheStore<String> otpCache, CacheStore<User> userCache, SimpleResponseStructure simpleResponseStructure,
			MailService mailService, AuthenticationManager authenticationManager, JwtService jwtService,
			AccessTokenRepository accessTokenRepository, RefreshTokenRepository refreshTokenRepository) {
		super();
		this.userRepository = userRepository;
		this.responseStructure = responseStructure;
		this.otpCache = otpCache;
		this.userCache = userCache;
		this.simpleResponseStructure = simpleResponseStructure;
		this.mailService = mailService;
		this.authenticationManager = authenticationManager;
		this.jwtService = jwtService;
		this.accessTokenRepository = accessTokenRepository;
		this.refreshTokenRepository = refreshTokenRepository;
	}

	@Override
	public ResponseEntity<SimpleResponseStructure> registerUser(UserRequest userRequest) throws MessagingException {
		if (userRepository.existsByEmail(userRequest.getEmail()))
			throw new UserAlreadyExistByEmailException("Registration failed");
		User user = mapToChildEntity(userRequest);
		String otp = generateOTP();

		otpCache.add(user.getEmail(), otp);
		userCache.add(user.getEmail(), user);
		sendMail(user, otp);

		return ResponseEntity.ok(simpleResponseStructure.setStatus(HttpStatus.ACCEPTED.value())
				.setMessage("Verify OTP sent through mail to complete registration| OTP expires in 1 minute "));
	}

	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> verifyOtp(OtpRequest otpRequest) {
		if (otpCache.get(otpRequest.getEmail()) == null)
			throw new OtpExpiredException("Otp expired");
		if (!otpCache.get(otpRequest.getEmail()).equals(otpRequest.getOtp()))
			throw new InvalidOtpException("Invalid otp");

		User user = userCache.get(otpRequest.getEmail());
		if (user == null)
			throw new RegistrationSessionExpiredException("Session expired");
		user.setEmailVerified(true);
		user = userRepository.save(user);

		return ResponseEntity.ok(responseStructure.setMessage("user Registeres as " + user.getUserRole())
				.setStatus(HttpStatus.OK.value()).setData(mapToUserResponse(user)));

	}

	public ResponseEntity<ResponseStructure<AuthResponse>> login(AuthRequest authRequest) {
        String userName = authRequest.getUserName().split("@gmail.com")[0]; 
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, authRequest.getPassword()));

        if (!authentication.isAuthenticated()) {
            throw new InvalidCredentialException("Invalid credentials");
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        HttpHeaders headers = new HttpHeaders();
        return userRepository.findByUserName(userName)
                .map(user -> {
                    
                    generateAccessToken(user,headers);
                    generateRefreshToken(user,headers);

                    AuthResponse authResponse = AuthResponse.builder()
                            .userId(user.getUserId())
                            .userName(userName)
                            .role(user.getUserRole())
                            .accessExpiration(accessExpiration)
                            .refreshExpiration(refeshExpiration)
                            .build();

                    ResponseStructure<AuthResponse> responseStructure = new ResponseStructure<>();
                    responseStructure.setMessage("Logged in successfull")
                                     .setStatus(HttpStatus.OK.value())
                                     .setData(authResponse);

                    return ResponseEntity.ok().headers(headers).body(responseStructure);
                }).get();
               // .orElseThrow(() -> new InvalidCredentialException("Invalid credentials"));
    }

	private void generateRefreshToken(User user, HttpHeaders headers) {
		String refreshToken = jwtService.generateRefreshToken(user);
		headers.add(HttpHeaders.SET_COOKIE, generateCookie("rt", refreshToken, refeshExpiration));
		refreshTokenRepository.save(RefreshToken.builder().isBlocked(false).token(refreshToken).expiration(accessExpiration).user(user).build());

	}

	private String generateCookie(String name, String value, long maxAge) {
		return ResponseCookie.from(name, value).domain("localhost").path("/").httpOnly(true).secure(false)
				.maxAge(Duration.ofMillis(maxAge)).sameSite("Lax").build().toString();

	}

	private void generateAccessToken(User user, HttpHeaders headers) {
		String accessToken = jwtService.generateAccessToken(user);
		headers.add(HttpHeaders.SET_COOKIE, generateCookie("at", accessToken, accessExpiration));
		accessTokenRepository.save(AccessToken.builder().isBlocked(false).token(accessToken).expiration(accessExpiration).user(user).build());
	}

	private void sendMail(User user, String otp) throws MessagingException {
		MessageModel model = MessageModel.builder().subject("Verify OTP").text("<!DOCTYPE html>" + "<html lang='en'>"
				+ "<head>" + "<meta charset='UTF-8'>"
				+ "<meta name='viewport' content='width=device-width, initial-scale=1.0'>"
				+ "<title>Email Template</title>" + "</head>" + "<body style='font-family: Arial, sans-serif;'>"
				+ "<div style='max-width: 600px; margin: 0 auto; padding: 20px;'>"
				+ "<h2 style='color: #333;'>Hi, <span style='color: #007bff;'>" + user.getDisplayName() + "</span></h2>"
				+ "<p>Thanks for your interest in E-Shopify. Please register yourself with the OTP given below:</p>"
				+ "<div style='background-color: #f5f5f5; padding: 15px; text-align: center; border-radius: 5px; margin-bottom: 20px;'>"
				+ "<h1 style='margin: 0; color: #007bff; font-size: 32px;'>" + otp + "</h1>" + "</div>"
				+ "<p>Please ignore this message if it wasn't intended for you.</p>"
				+ "<div style='border-top: 2px solid #007bff; margin-top: 20px; padding-top: 10px;'>"
				+ "<h1 style='margin: 0; color: #007bff; font-size: 24px;'>Best Regards,</h1>"
				+ "<h3 style='margin: 5px 0 0; color: #333;'>E-Shopify Team</h3>" + "</div>"
				+ "<div style='text-align: center; margin-top: 20px;'>"
				+ "<img src='https://evincedev.com/blog/wp-content/uploads/2023/08/Shopify-eCommerce-Development.jpg' alt='E-Shopify' width='300' height='150'>"
				+ "</div>" + "</div>" + "</body>" + "</html>").to(user.getEmail()).build();
		mailService.sendMailMessage(model);
	}

	private String generateOTP() {
		return String.valueOf(new Random().nextInt(100000, 999999));
	}

	@SuppressWarnings("unchecked")
	private <T extends User> T mapToChildEntity(UserRequest userRequest) {
		UserRole role = userRequest.getUserRole();
		User user = null;
		if (role != null) {
			switch (role) {
			case SELLER -> user = new Seller();
			case CUSTOMER -> user = new Customer();
			default -> throw new InvalidRoleException("Invalid role");
			}
		} else
			throw new InvalidRoleException("");
		user.setUserName(userRequest.getEmail().split("@gmail.com")[0]);
		user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
		user.setUserRole(userRequest.getUserRole());
		user.setEmail(userRequest.getEmail());
		user.setDisplayName(userRequest.getDisplayName());

		return (T) user;
	}

	private UserResponse mapToUserResponse(User user) {
		return UserResponse.builder().userId(user.getUserId()).email(user.getEmail()).userName(user.getUserName()).userRole(user.getUserRole())
				.userName(user.getUserName()).displayName(user.getDisplayName()).isEmailVerified(user.isEmailVerified())
				.isDeleted(user.isDeleted()).build();
	}
}
