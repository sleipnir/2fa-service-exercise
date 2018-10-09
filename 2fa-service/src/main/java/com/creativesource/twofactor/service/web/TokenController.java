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

import com.creativesource.twofactor.service.model.TokenRequest;
import com.creativesource.twofactor.service.model.TokenResponse;
import com.creativesource.twofactor.service.model.TokenResponse.TokenResponseBuilder;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auth/tokens")
public class TokenController {
	
	private final TokenService tokenService;
	
	@Autowired
	public TokenController(final TokenService tokenService) {
		this.tokenService = tokenService;
	}
	
	/*
	 * Request Example:
	 * curl -X POST \
	 *	  http://localhost:8080/auth/tokens \
	 *	  -H 'Authorization: Basic c2Vuc2VkaWE6c2Vuc2VkaWEqMTIz' \
	 *	  -H 'Content-Type: application/json' \
	 *	  -d '{
	 *		"countryCode" : "+55",
	 *		"areaCode" : "11",
	 *		"phoneNumber" : "959734939",
	 *		"tokenTTL" : 300
	 *	}'
	 * */
	@PostMapping
	public Mono<ResponseEntity<TokenResponse>> createToken(@RequestBody @Valid TokenRequest tokenRequest){
		return tokenService.sendToken(tokenRequest)
					.flatMap(this::handleStatus)
					.doOnError(this::errorHandler);
	}
	
	/*
	 * curl -X GET http://localhost:8080/auth/tokens?code=569868 -H 'Authorization: Basic c2Vuc2VkaWE6c2Vuc2VkaWEqMTIz'
	 * */
	@GetMapping
	public Mono<ResponseEntity<String>> validateToken(@RequestParam("code") String code){
		return Mono.just(ResponseEntity.ok("OK"));
	}
	
	private Mono<ResponseEntity<TokenResponse>> handleStatus(ClientResponse response){
		TokenResponseBuilder builder = TokenResponse.builder();
	
		if(!isSuccessful(response)) {
			//return error message
			builder.code(422);
			builder.message("Nao foi possivel processar sua solicitacao");
			return Mono.just(ResponseEntity.unprocessableEntity().body(builder.build()));
		}
		
		builder.code(202);
		builder.message("Token encaminhado ao destinatario");
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
						.message(String.format("Nao foi possivel processar sua solicitacao. %s", error.getMessage()))
						.build()));
	}

}
