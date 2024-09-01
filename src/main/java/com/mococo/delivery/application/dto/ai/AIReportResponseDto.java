package com.mococo.delivery.application.dto.ai;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
@Getter
@Builder
public class AIReportResponseDto {
    private String question;
    private String result;
    private LocalDateTime createdAt;
    private String createdBy;
}
