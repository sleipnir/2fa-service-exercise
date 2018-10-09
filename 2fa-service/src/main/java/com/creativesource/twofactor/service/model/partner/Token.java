
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
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "ttl", "redirectTo", "mode", "acceptedOnlyFromSameDomain" })
public class Token implements Serializable {
	private final static long serialVersionUID = 6931873650137333560L;

	@Getter
	@NotNull
	@JsonProperty("ttl")
	public Integer ttl;

	@Getter
	@JsonProperty("redirectTo")
	public String redirectTo;
	
	@Getter
	@NotNull
	@JsonProperty("mode")
	public String mode;

	@Getter
	@JsonProperty("acceptedOnlyFromSameDomain")
	public Boolean acceptedOnlyFromSameDomain;
	
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
