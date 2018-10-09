package com.creativesource.twofactor.service;

import static com.creativesource.twofactor.service.HttpUtils.logRequest;
import static com.creativesource.twofactor.service.HttpUtils.logResposneStatus;

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

import reactor.core.publisher.Mono;

@RunWith(SpringRunner.class)
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT, classes = {Application.class, ApplicationSecurityConfig.class})
public class WebHookControllerTests {
	
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
			  .filter(logRequest())
			  .filter(logResposneStatus())
			  .build();
	}
	
	@Test
	public void webHookTest() {
		webTestClient
			.post()
			.uri("/auth/tokens/events")
			.body(Mono.just("{}"), String.class)
			.exchange()
				.expectStatus().isOk();
	}

}
