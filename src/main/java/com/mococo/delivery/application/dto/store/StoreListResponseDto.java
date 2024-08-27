package com.mococo.delivery.application.dto.store;

import java.util.List;

import com.mococo.delivery.application.dto.PageInfoDto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StoreListResponseDto {
	private List<StoreResponseDto> storeList;
	private PageInfoDto pageInfo;
}
