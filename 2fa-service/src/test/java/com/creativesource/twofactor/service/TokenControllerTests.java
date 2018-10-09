package com.creativesource.twofactor.service;

import static com.creativesource.twofactor.service.HttpUtils.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions;

import com.creativesource.twofactor.service.model.TokenRequest;

import reactor.core.publisher.Mono;

@RunWith(SpringRunner.class)
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT, classes = {Application.class, ApplicationSecurityConfig.class})
public class TokenControllerTests {
	
	@Value("${server.port:8080}")
	private int port;
	
	@Value("${app.partner.user:sensedia}")
	private String username;
	
	@Value("${app.partner.token}")
	private String token;
	
	@Autowired
	private WebTestClient webTestClient;
	
	@Before
	public void setup() {
		webTestClient = WebTestClient
				  .bindToServer()
				  .baseUrl(String.format("http://localhost:%s", port))
				  .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE)
				  .filter(ExchangeFilterFunctions.basicAuthentication(username, token))
				  .filter(logRequest())
				  .filter(logResposneStatus())
				  .build();
	}
	
	@Test
	public void sendTokenTest() {
		TokenRequest requestBody = TokenRequest.builder()
				.countryCode("+55")
				.areaCode("11")
				.phoneNumber("959734939")
				.tokenTTL(300)
				.build();
				
		webTestClient
			.post()
			.uri("/auth/tokens")
			.body(Mono.just(requestBody), TokenRequest.class)
			.exchange()
				.expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
				.expectStatus().isAccepted()
				.expectBody()
				.jsonPath("$.code").isNotEmpty()
				.jsonPath("$.message").isEqualTo("Token encaminhado ao destinatario");
	}
	
	@Test
	public void tokenNotFoundTest() {
		webTestClient
			.get()
			.uri(uriBuilder -> uriBuilder.path("/auth/tokens")
                    .queryParam("code", 999999)
                    .build())
			.exchange()
				.expectStatus().isNotFound();
	}
	
	

}
