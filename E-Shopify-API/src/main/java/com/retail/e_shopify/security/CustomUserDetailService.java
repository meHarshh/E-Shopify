package com.retail.e_shopify.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.retail.e_shopify.repository.UserRepository;

import lombok.AllArgsConstructor;


@AllArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService{

	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		return userRepository.findByUserName(username).map(CustomUserDetails::new)//method reference operator
				.orElseThrow(()-> new UsernameNotFoundException("Invalid UserName"));
	}



}



