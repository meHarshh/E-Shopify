package com.retail.e_shopify.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Builder
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccessToken {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int tokenId;
	private String token;
	private boolean isBlocked;
	private long expiration;
	
	@ManyToOne
	private User user;
}
