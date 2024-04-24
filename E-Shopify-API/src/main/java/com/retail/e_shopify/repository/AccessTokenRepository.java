package com.retail.e_shopify.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.retail.e_shopify.entity.AccessToken;

public interface AccessTokenRepository extends JpaRepository<AccessToken, Integer> {

	Optional<AccessToken> findByToken(String at);

	boolean existsByTokenAndIsBlocked(String at, boolean b);

}