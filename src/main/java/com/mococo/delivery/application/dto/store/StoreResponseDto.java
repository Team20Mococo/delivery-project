package com.mococo.delivery.application.dto.store;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class StoreResponseDto {
    private UUID storeId;
    private String username;
    private String name;
    private String category;
    private String notice;
    private String description;
    private LocalDateTime createdAt;
    private String createdBy;
}
