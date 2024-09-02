package com.mococo.delivery.application.dto.store;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStoreStatusRequestDto {
	private Boolean isOperating;
}