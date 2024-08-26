package com.mococo.delivery.application.dto.category;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CategoryResponseDto {
    private String name;
    private LocalDateTime createdAt;
    private String createdBy;
}
