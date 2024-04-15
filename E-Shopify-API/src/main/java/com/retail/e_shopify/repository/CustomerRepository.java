package com.retail.e_shopify.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.retail.e_shopify.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

}
