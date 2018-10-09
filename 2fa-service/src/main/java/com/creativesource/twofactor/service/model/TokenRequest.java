package com.creativesource.twofactor.service.model;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Builder
@ToString
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "countryCode", "areaCode", "phoneNumber", "tokenTTL" })
public @Data class TokenRequest implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Builder.Default private String countryCode = "+55"; 
	@Builder.Default private String areaCode = "11";
	private @NotNull String phoneNumber;
	@Builder.Default private int tokenTTL = 300;
}
