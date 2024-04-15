package com.retail.e_shopify.utility;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
@OpenAPIDefinition
public class ApplicationDoc {

	Contact contact() {
		return new Contact().name("Harsh")
				.email("harsh@gmail.com")
				.url("http://localhost:5173/");
	}
	
	@Bean
	Info info() {
		return new Info().title("E-Shopify")
		.description("An Advance Level Online shopping Application using Spring Boot RESTful APIs and React, dealing with several real world scenarios and workflows. The application equips the complete authentication and authorization workflows, Seller Dashboard Management, product catalog, cart and order processing and search operations based on several criteria's. ")
		.contact(contact()).version("v1");
	}
	
	@Bean
	OpenAPI openAPI() {
		return new OpenAPI().info(info());
	}

	
}
