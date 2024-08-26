package com.mococo.delivery.application.dto.store;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StoreRequestDto {
    private String username;
    private String name;
    private String category;
    private String notice;
    private String description;
}
