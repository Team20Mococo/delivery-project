package com.mococo.delivery.application.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PageInfoDto {
	private long totalItems;
	private int totalPages;
	private int currentPage;
	private int pageSize;
	private boolean hasNextPage;
}
