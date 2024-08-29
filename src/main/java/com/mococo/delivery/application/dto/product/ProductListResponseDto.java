package com.mococo.delivery.application.dto.product;

import java.util.List;

import com.mococo.delivery.application.dto.PageInfoDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductListResponseDto {
	private List<ProductSimpleResponseDto> productList;
	private PageInfoDto pageInfo;
}