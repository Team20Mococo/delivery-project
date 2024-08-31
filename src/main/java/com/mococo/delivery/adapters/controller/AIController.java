package com.mococo.delivery.adapters.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mococo.delivery.application.dto.SuccessResponseDto;
import com.mococo.delivery.application.dto.ai.AIQuestionRequestDto;
import com.mococo.delivery.application.dto.ai.AIResponseDto;
import com.mococo.delivery.application.service.AIService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/owner/ai")
@RequiredArgsConstructor
public class AIController {

	private final AIService aiService;

	@PostMapping("")
	public SuccessResponseDto<AIResponseDto> askAI(@RequestBody AIQuestionRequestDto request) {
		AIResponseDto response = aiService.askAI(request);
		return new SuccessResponseDto<>("ai 답변 요청에 성공했습니다.", response);
	}

}
