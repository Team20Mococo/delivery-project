package com.mococo.delivery.adapters.controller;

import java.net.http.HttpResponse;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mococo.delivery.application.dto.SuccessResponseDto;
import com.mococo.delivery.application.dto.user.LoginRequestDto;
import com.mococo.delivery.application.dto.user.SignUpRequestDto;
import com.mococo.delivery.application.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController()
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController {

	private final UserService userService;

	@PostMapping("/users/sign-up")
	public SuccessResponseDto<Boolean> signUp(@RequestBody SignUpRequestDto request) {
		Boolean response = userService.signUp(request);
		return new SuccessResponseDto<>("회원가입에 성공했습니다", response);
	}

	@PostMapping("/users/log-in")
	public SuccessResponseDto<Boolean> login(HttpServletResponse response, @RequestBody LoginRequestDto request) {
		Boolean result = userService.login(response, request);
		return new SuccessResponseDto<>("로그인에 성공했습니다", result);
	}
}
