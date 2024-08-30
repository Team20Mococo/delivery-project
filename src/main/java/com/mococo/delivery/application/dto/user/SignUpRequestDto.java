package com.mococo.delivery.application.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequestDto {
	private String username;
	private String nickname;
	private String email;
	private String address;
	private String password;
}
