package com.mococo.delivery.adapters.controller;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mococo.delivery.application.dto.SuccessResponseDto;
import com.mococo.delivery.application.dto.user.LoginRequestDto;
import com.mococo.delivery.application.dto.user.SignUpRequestDto;
import com.mococo.delivery.application.dto.user.UserListResponseDto;
import com.mococo.delivery.application.dto.user.UserPutRequestDto;
import com.mococo.delivery.application.dto.user.UserResponseDto;
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

	@GetMapping("/admin/users/{username}")
	public SuccessResponseDto<UserResponseDto> getUser(@PathVariable String username) {
		return new SuccessResponseDto<>("유저 정보 조회에 성공했습니다", userService.getUser(username));
	}

	@PutMapping("/users/{username}")
	public SuccessResponseDto<UserResponseDto> modifyUser(
		@PathVariable String username,
		@RequestBody UserPutRequestDto requestDto) {
		return new SuccessResponseDto<>("수정에 성공했습니다", userService.updateUser(username, requestDto));
	}

	@DeleteMapping("/users/{username}")
	public SuccessResponseDto<Boolean> deleteUser(@PathVariable String username) {
		return new SuccessResponseDto<>("삭제 성공", userService.deleteUser(username));
	}

	@GetMapping("/admin/users")
	public SuccessResponseDto<UserListResponseDto> getUsers(
		@RequestParam(required = false) String sortBy,
		@RequestParam(defaultValue = "asc") String direction,
		@RequestParam(required = false) boolean filter,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size,
		@RequestParam(required = false) String searchQuery
	) {
		return new SuccessResponseDto<>("조회 성공",
			userService.getAllUsers(sortBy, direction, filter, page, size, searchQuery));
	}
}
