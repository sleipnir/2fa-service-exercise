package com.creativesource.twofactor.service.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Builder
@ToString
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public @Data class TokenResponse implements Serializable{
	private static final long serialVersionUID = 1L;
	private int code;
	private String message;
}
