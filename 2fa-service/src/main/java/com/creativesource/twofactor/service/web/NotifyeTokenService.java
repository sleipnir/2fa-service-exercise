package com.creativesource.twofactor.service.web;

import static com.creativesource.twofactor.service.HttpUtils.logRequest;
import static com.creativesource.twofactor.service.HttpUtils.logResposneStatus;

import java.net.URI;
import java.util.Objects;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import com.creativesource.twofactor.service.TokenService;
import com.creativesource.twofactor.service.model.TokenRequest;
import com.creativesource.twofactor.service.model.partner.Message;
import com.creativesource.twofactor.service.model.partner.NotifyeRequest;
import com.creativesource.twofactor.service.model.partner.PhoneNumber;
import com.creativesource.twofactor.service.model.partner.Token;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public final class NotifyeTokenService implements TokenService {
	
	private static final String HOOK_URL_ENV_KEY = "HOOK_URL"; //$NON-NLS-1$
	private static final String CONTENT_TYPE_HEADER = "Content-Type"; //$NON-NLS-1$
	private static final String AUTHORIZATION_HEADER = "Authorization"; //$NON-NLS-1$
	
	private final String partnerTokenDomainUrl;
	private final String resourceURI;
	private final String partnerToken;
	
	private WebClient client;
	
	public NotifyeTokenService(@Value("${app.partner.token.baseUrl}") final String partnerTokenDomainUrl, 
						@Value("${app.partner.token.createRequestUrl}") final String resourceURI,
						@Value("${app.partner.token.authToken}") final String partnerToken) {
		
		this.partnerTokenDomainUrl = partnerTokenDomainUrl;
		this.resourceURI = resourceURI;
		this.partnerToken = partnerToken;
	}
	
	@PostConstruct
	public void setup() {
		this.client = WebClient.builder()
				.baseUrl(partnerTokenDomainUrl)
				.defaultHeader(AUTHORIZATION_HEADER, String.format("Bearer %s", partnerToken)) //$NON-NLS-1$
				.defaultHeader(CONTENT_TYPE_HEADER, MediaType.APPLICATION_JSON_VALUE)
				.filter(logRequest())
				.filter(logResposneStatus())
				.build();
	}
	
	/* (non-Javadoc)
	 * @see com.creativesource.twofactor.service.web.TokenService#sendToken(com.creativesource.twofactor.service.model.TokenRequest)
	 */
	@Override
	public Mono<ClientResponse> sendToken(TokenRequest request) {
		log.debug("Send request to partner"); //$NON-NLS-1$
		String uriRequest = String.format("%s%s", partnerTokenDomainUrl, resourceURI); //$NON-NLS-1$
		return client
				.post()
				.uri(URI.create(uriRequest))
				.body(Mono.just(createRequestBody(request)), NotifyeRequest.class)
				.accept(MediaType.APPLICATION_JSON)
					.exchange();
	}
	
	@Override
	public Mono<ClientResponse> getTokenStatus(String code) {
		return client
				.get()
				.uri(uriBuilder -> uriBuilder.path(resourceURI)
	                    .queryParam("code", code)
	                    .build())
				.accept(MediaType.APPLICATION_JSON)
			  		.exchange();
	}

	
	private NotifyeRequest createRequestBody(TokenRequest tokenRequest) {
		return NotifyeRequest.builder()
				.from(getFrom())
				.to(getTo(tokenRequest))
				.message(new Message(Messages.getString("Messages.ENTER_CODE_MSG"))) //$NON-NLS-1$
				.token(getToken(tokenRequest))
				.hook(getHook())
				.build();
	}

	private String getHook() {
		String hookUrl = System.getenv(HOOK_URL_ENV_KEY);
		log.debug("Adding HOOK URL: {}", hookUrl); //$NON-NLS-1$
		return ( Objects.nonNull(hookUrl) ? hookUrl : "http://example.com" ); //$NON-NLS-1$
	}

	private Token getToken(TokenRequest tokenRequest) {
		return Token.builder()
				.ttl( (tokenRequest.getTokenTTL() > 0 ? tokenRequest.getTokenTTL() : 300) )
				.mode("Passive") //$NON-NLS-1$
				.build();
	}

	private PhoneNumber getTo(TokenRequest tokenRequest) {
		return PhoneNumber.builder()
				.countryCode(tokenRequest.getCountryCode())
				.areaCode(tokenRequest.getAreaCode())
				.number(buildPhoneNumber(tokenRequest))
				.build();
	}

	private PhoneNumber getFrom() {
		return PhoneNumber.builder()
				.countryCode("+55") //$NON-NLS-1$
				.areaCode("11") //$NON-NLS-1$
				.number("999999999") //$NON-NLS-1$
				.build();
	}
	
	private String buildPhoneNumber(TokenRequest tokenRequest) {
		return new StringBuilder()
				.append(tokenRequest.getAreaCode())
				.append(tokenRequest.getPhoneNumber())
				.toString();
	}
	
}
