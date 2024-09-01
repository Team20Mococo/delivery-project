package com.mococo.delivery.adapters.controller;

import com.mococo.delivery.application.dto.ai.AIListResponseDto;
import org.springframework.web.bind.annotation.*;

import com.mococo.delivery.application.dto.SuccessResponseDto;
import com.mococo.delivery.application.dto.ai.AIQuestionRequestDto;
import com.mococo.delivery.application.dto.ai.AIResponseDto;
import com.mococo.delivery.application.service.AIService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AIController {

	private final AIService aiService;

	@PostMapping("/owner/ai")
	public SuccessResponseDto<AIResponseDto> askAI(@RequestBody AIQuestionRequestDto request) {
		AIResponseDto response = aiService.askAI(request);
		return new SuccessResponseDto<>("ai 답변 요청에 성공했습니다.", response);
	}

	@GetMapping("/admin/ai")
	public SuccessResponseDto<AIListResponseDto> getAIReportList(
			@RequestParam(required = false) String sortBy,
			@RequestParam(defaultValue = "asc") String direction,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(required = false) String searchQuery
	) {
		AIListResponseDto response = aiService.getAllAIReports(sortBy, direction, page, size, searchQuery);
		return new SuccessResponseDto<>("ai 답변 리스트 조회에 성공했습니다", response);
	}

}
