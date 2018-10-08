
package com.creativesource.twofactor.service.model.partner;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Builder;
import lombok.Data;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "phoneNumber", "to", "message", "token", "hook" })
public @Data class NotifyeRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	@Valid
	@NotNull
	@JsonProperty("from")
	public PhoneNumber from;
	
	@Valid
	@NotNull
	@JsonProperty("to")
	public PhoneNumber to;
	
	@Valid
	@NotNull
	@JsonProperty("message")
	public Message message;

	@Valid
	@NotNull
	@JsonProperty("token")
	public Token token;

	@NotNull
	@JsonProperty("hook")
	public String hook;
	
	@Valid
	@JsonIgnore
	@Builder.Default
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}
