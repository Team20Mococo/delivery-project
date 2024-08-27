package com.mococo.delivery.domain.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.mococo.delivery.domain.model.enumeration.OrderStatus;
import com.mococo.delivery.domain.model.enumeration.OrderType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "p_orders")
@Getter
@Setter
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID orderId;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne
	@JoinColumn(name = "store_id", nullable = false)
	private Store store;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private OrderType orderType;  // 주문 유형 (예: 온라인, 직접)

	@Column(nullable = false)
	private Integer totalPrice;  // 주문의 총 가격

	@Column(length = 255, nullable = false)
	private String address;  // 주문 배송 주소

	@Column(columnDefinition = "TEXT")
	private String request;  // 주문 요청 사항

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private OrderStatus orderStatus = OrderStatus.READY;  // 주문 상태 (예: 준비중, 완료)

	@Column(nullable = false)
	private LocalDateTime createdAt;  // 주문 생성 시간

	@Column(length = 100)
	private String createdBy;  // 주문 생성자

	@Column
	private LocalDateTime updatedAt;  // 주문 수정 시간

	@Column(length = 100)
	private String updatedBy;  // 주문 수정자

	@Column
	private LocalDateTime deletedAt;  // 주문 삭제 시간 (실제 삭제 아님)

	@Column(length = 100)
	private String deletedBy;  // 주문 삭제자

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<OrderProduct> orderProducts;  // 주문에 포함된 여러 제품

	@OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
	private Payment payment;

	@OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
	private Delivery delivery;

	// 주문 취소 로직
	public void cancelOrder() {
		if (this.orderStatus == OrderStatus.READY && // 이게 주문상태가 READY일떄만 취소 가능하고
			LocalDateTime.now().isBefore(this.createdAt.plusMinutes(5))) {
			this.orderStatus = OrderStatus.CANCELLED;
			this.deletedAt = LocalDateTime.now();
			this.deletedBy = this.user.getUsername();
		} else {
			throw new IllegalStateException("주문은 5분 이내에만 취소할 수 있습니다.");
		}
	}
}




