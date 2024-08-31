package com.mococo.delivery.application.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserPutRequestDto {
	private String nickname;
	private String address;
	@JsonProperty("public")
	private boolean isPublic;
}
