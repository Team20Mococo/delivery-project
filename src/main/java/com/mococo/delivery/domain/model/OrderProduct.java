package com.mococo.delivery.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "p_orders_products")
@Getter
@Setter
public class OrderProduct {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "order_id", nullable = false)
	private Order order;

/*    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;*/

	@Column(nullable = false)
	private Integer quantity;  // 주문한 제품의 수량
}






