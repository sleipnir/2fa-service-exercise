package com.creativesource.twofactor.service.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.ClientResponse;

import com.creativesource.twofactor.service.TokenService;
import com.creativesource.twofactor.service.model.TokenRequest;
import com.creativesource.twofactor.service.model.TokenResponse;
import com.creativesource.twofactor.service.model.TokenResponse.TokenResponseBuilder;
import com.creativesource.twofactor.service.model.TokenStatus;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auth/tokens")
public class TokenController {
	
	private final TokenService tokenService;
	
	@Autowired
	public TokenController(final TokenService tokenService) {
		this.tokenService = tokenService;
	}
	
	@PostMapping
	public Mono<ResponseEntity<TokenResponse>> createToken(@RequestBody @Valid TokenRequest tokenRequest){
		return tokenService.sendToken(tokenRequest)
					.flatMap(this::handleStatus)
					.doOnError(this::errorHandler);
	}
	
	@GetMapping
	public Mono<ResponseEntity<TokenStatus>> validateToken(@RequestParam("code") String code){
		return tokenService.getTokenStatus(code)
				.flatMap(response -> response.bodyToMono(TokenStatus.class))
				.flatMap(tokenStatus -> Mono.just(ResponseEntity.ok(tokenStatus)))
				.switchIfEmpty(
						Mono.just(
								ResponseEntity.notFound().build()));
	}
	
	private Mono<ResponseEntity<TokenResponse>> handleStatus(ClientResponse response){
		TokenResponseBuilder builder = TokenResponse.builder();
	
		if(!isSuccessful(response)) {
			builder.code(422);
			builder.message(Messages.getString("Messages.NOT_PROCESS_SOLICITATION_MSG"));
			return Mono.just(ResponseEntity.unprocessableEntity().body(builder.build()));
		}
		
		builder.code(202);
		builder.message(Messages.getString("Messages.TOKEN_FORWARD_MSG"));
		return Mono.just(ResponseEntity.accepted().body(builder.build()));
	}

	private boolean isSuccessful(ClientResponse response) {
		return response.statusCode().is2xxSuccessful();
	}
	
	private Mono<ResponseEntity<TokenResponse>> errorHandler(Throwable error){
		return Mono.just(
				ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(
					TokenResponse.builder()
						.code(500)
						.message(String.format(Messages.getString("Messages.NOT_PROCESS_SOLICITATION_REASON_MSG"), error.getMessage()))
						.build()));
	}

}
