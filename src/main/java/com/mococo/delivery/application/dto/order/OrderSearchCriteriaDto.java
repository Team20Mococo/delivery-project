package com.mococo.delivery.application.dto.order;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderSearchCriteriaDto {
	private String type;      // 주문 유형 ("ONLINE", "DIRECT")
	private String status;    // 주문 상태 ("READY", "ACCEPTED", "CANCELLED", "COMPLETED")
	private String storeName; // 상점 이름
	private String username;  // 사용자 이름
}
