package com.creativesource.twofactor.service.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping
public class WebHookController {
	
	@PostMapping("/auth/tokens/events")
	public Mono<ResponseEntity<?>> event(@RequestBody String request){
		log.debug("Request {}", request);
		return Mono.just(ResponseEntity.ok().build());
	}
	
}
