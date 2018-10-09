package com.creativesource.twofactor.service;

import org.springframework.web.reactive.function.client.ClientResponse;

import com.creativesource.twofactor.service.model.TokenRequest;

import reactor.core.publisher.Mono;

public interface TokenService {
	Mono<ClientResponse> sendToken(TokenRequest request);
	Mono<ClientResponse> getTokenStatus(String code);
}