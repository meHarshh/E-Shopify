package com.retail.e_shopify.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.retail.e_shopify.entity.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer>{

	boolean existsByTokenAndIsBlocked(String rt, boolean b);
	
}
