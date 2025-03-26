package com.retail.e_shopify.cache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.retail.e_shopify.entity.User;

@Configuration
public class CacheBeanConfig {
	
	@Bean
	CacheStore<String> otpCache(){
		return new CacheStore<String>(10);
	}
	
	@Bean
	CacheStore<User> userCache(){
		return new CacheStore<User>(30);
	}
}
