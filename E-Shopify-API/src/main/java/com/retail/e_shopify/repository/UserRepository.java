package com.retail.e_shopify.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.retail.e_shopify.entity.User;

public interface UserRepository extends JpaRepository<User, Integer>{

	boolean existsByEmail(String email);

}
