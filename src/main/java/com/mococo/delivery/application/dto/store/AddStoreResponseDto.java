package com.mococo.delivery.application.dto.store;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AddStoreResponseDto {
    private StoreResponseDto store;
}
