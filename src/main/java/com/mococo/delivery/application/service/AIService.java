package com.mococo.delivery.application.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mococo.delivery.application.dto.ai.AIQuestionRequestDto;
import com.mococo.delivery.application.dto.ai.AIResponseDto;
import com.mococo.delivery.domain.exception.entity.UserNotFoundException;
import com.mococo.delivery.domain.model.AIReport;
import com.mococo.delivery.domain.model.User;
import com.mococo.delivery.domain.repository.AIReportRepository;
import com.mococo.delivery.domain.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AIService {

	private final AIReportRepository aiReportRepository;
	private final UserRepository userRepository;
	private final AuditorAwareImpl auditorAware;
	private final RestTemplate restTemplate = new RestTemplate();
	private static final int MAX_TEXT_LENGTH = 100; // 입력 텍스트 최대 길이 제한

	@Value("${GOOGLE_API_KEY}")
	private String apiKey;

	@Transactional
	public AIResponseDto askAI(AIQuestionRequestDto request) {
		String currentUser = auditorAware.getCurrentAuditor().orElse("system");

		User user = userRepository.findByUsername(currentUser)
			.orElseThrow(UserNotFoundException::new);

		String question = request.getQuestion();
		if (question.length() > MAX_TEXT_LENGTH) {
			question = question.substring(0, MAX_TEXT_LENGTH);
		}

		String finalQuestion = question + " 답변을 최대한 간결하게 50자 이하로";

		String aiResponse = getAIResponse(finalQuestion);

		AIReport aiReport = AIReport.builder()
			.user(user)
			.question(request.getQuestion())
			.result(aiResponse)
			.build();

		AIReport savedAIReport = aiReportRepository.save(aiReport);

		return AIResponseDto.builder()
			.result(savedAIReport.getResult())
			.build();
	}

	private String getAIResponse(String question) {
		String apiUrl =
			"https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent?key="
				+ apiKey;
		String requestBody = "{ \"contents\": [ { \"parts\": [ { \"text\": \"" + question + "\" } ] } ] }";

		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");

		HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
		ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, String.class);

		ObjectMapper objectMapper = new ObjectMapper();
		try {
			JsonNode rootNode = objectMapper.readTree(response.getBody());
			return rootNode.path("candidates").get(0).path("content").path("parts").get(0).path("text").asText();
		} catch (Exception e) {
			throw new RuntimeException("AI 응답을 처리하는 중 오류가 발생했습니다.", e);
		}
	}
}
