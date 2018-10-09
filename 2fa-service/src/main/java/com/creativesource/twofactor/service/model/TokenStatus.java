package com.creativesource.twofactor.service.model;

import java.io.Serializable;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

@Value
@Builder
@ToString
@EqualsAndHashCode
public class TokenStatus implements Serializable {
	private static final long serialVersionUID = 1L;
	private boolean valid;
	private long offset;
	private String message;
}
