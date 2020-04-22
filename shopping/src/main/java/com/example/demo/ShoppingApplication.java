package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan({ "com.spring.controller", "com.spring.util", "com.spring.service"})
@EntityScan("com.spring.model")
@EnableJpaRepositories("com.spring.repository")
public class ShoppingApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(ShoppingApplication.class, args);
	}
	
	/**
	 * Used when running as a WAR within a web container
	 * @param builder
	 * @return
	 */
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(ShoppingApplication.class);
	}
}
