package com.mococo.delivery.application.dto.category;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AddCategoryResponseDto {
    private CategoryResponseDto category;
}
