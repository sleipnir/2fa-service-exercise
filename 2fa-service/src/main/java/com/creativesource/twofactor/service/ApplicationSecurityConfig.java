package com.creativesource.twofactor.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class ApplicationSecurityConfig {

	@Bean
	@SuppressWarnings("deprecation")
	public MapReactiveUserDetailsService userDetailsRepository() {
		UserDetails user = User.withDefaultPasswordEncoder()
				.username("sensedia")
				.password("sensedia*123")
				.roles("USER")
				.build();
		return new MapReactiveUserDetailsService(user);
	}

	@Bean
	public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
		http.csrf().disable().authorizeExchange()
			.anyExchange()
				.authenticated()
					.and().httpBasic();
		return http.build();
	}
}