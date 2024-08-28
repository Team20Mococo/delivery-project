package com.mococo.delivery.application.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserPutRequestDto {
	private String nickname;
	private String address;
	private boolean isPublic;
}
