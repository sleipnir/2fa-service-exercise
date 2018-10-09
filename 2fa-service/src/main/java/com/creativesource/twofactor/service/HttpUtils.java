package com.creativesource.twofactor.service;

import org.springframework.web.reactive.function.client.ExchangeFilterFunction;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public final class HttpUtils {
	
	private HttpUtils() {}
	
	public static ExchangeFilterFunction logRequest() {
		return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
	        log.debug("Request: {} {}", clientRequest.method(), clientRequest.url());
	        clientRequest.headers()
	                .forEach((name, values) -> values.forEach(value -> log.debug("{}={}", name, value)));
	        return Mono.just(clientRequest);
	    });
	}
	
	public static ExchangeFilterFunction logResposneStatus() {
	    return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
	        log.debug("Response Status {}", clientResponse.statusCode());
	        clientResponse.headers().asHttpHeaders()
            	.forEach((name, values) -> values.forEach(value -> log.debug("{}={}", name, value)));
	        return Mono.just(clientResponse);
	    });
	}

}
