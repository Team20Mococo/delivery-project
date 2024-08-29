package com.mococo.delivery.application.dto.product;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateProductPublicStatusRequestDto {
    private Boolean isPublic;
}
