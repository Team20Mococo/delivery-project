package com.mococo.delivery.domain.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.mococo.delivery.domain.model.enumeration.OrderStatus;

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
	private OrderStatus orderStatus;

	@Column(nullable = false)
	private Integer totalPrice;

	@Column(length = 255, nullable = false)
	private String address;

	@Column(columnDefinition = "TEXT")
	private String request;

	@Column(nullable = false)
	private LocalDateTime createdAt;

	@Column(length = 100)
	private String createdBy;

	@Column
	private LocalDateTime updatedAt;

	@Column(length = 100)
	private String updatedBy;

	@Column
	private LocalDateTime deletedAt;

	@Column(length = 100)
	private String deletedBy;

	@OneToMany(mappedBy = "order")
	private List<OrderProduct> orderProducts;

	// 배달 및 결제 관련 코드를 주석 처리합니다.
    /*
    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private Payment payment;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private Delivery delivery;
    */

	// 주문 취소 로직
	public void cancelOrder() {
		if (this.orderStatus == OrderStatus.READY &&
			LocalDateTime.now().isBefore(this.createdAt.plusMinutes(5))) {
			this.orderStatus = OrderStatus.CANCELLED;
			this.deletedAt = LocalDateTime.now();
			this.deletedBy = this.user.getUsername();
		} else {
			throw new IllegalStateException("주문은 5분 이내에만 취소할 수 있습니다.");
		}
	}
}
