package com.retail.e_shopify.jwt;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.retail.e_shopify.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.lang.Maps;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	@Value("${myapp.jwt.secret}")
	private String secret;
	
	@Value("${myapp.jwt.access.expiration}")
	private long accessExpiration;
	
	@Value("${myapp.jwt.refresh.expiration}")
	private long refeshExpiration;

	public String generateAccessToken(User user) {
		return generateToken(user.getUserName(),user.getUserRole().name(),accessExpiration);
	}
	
	public String generateRefreshToken(User user) {
		return generateToken(user.getUserName(),user.getUserRole().name(), refeshExpiration);
	}
	
	private String generateToken(String userName, String role, long accessExpiration2) {
		return	Jwts.builder()
				.setSubject(userName)
				.setClaims(Maps.of(userName, role).build())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + accessExpiration2 ))
				.signWith(getSignatureKey(),SignatureAlgorithm.HS256)
				.compact();
	}

	private Key getSignatureKey() {
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
	}
	
	public String getUserName(String jwtToken) {
		return getJwtClaim(jwtToken).getSubject();
	}
	
	private Claims getJwtClaim(String jwtToken) {
		return Jwts.parserBuilder().setSigningKey(getSignatureKey()).build().parseClaimsJws(jwtToken).getBody();	
	}
	
	public String getRole(String jwtToken) {
		return getJwtClaim(jwtToken).get("role",String.class);
	}

}