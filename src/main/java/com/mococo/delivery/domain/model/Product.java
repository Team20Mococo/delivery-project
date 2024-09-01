package com.mococo.delivery.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder(toBuilder = true)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "p_product")
public class Product extends Auditable {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "product_id")
	private UUID id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private Integer price;

	@Column(nullable = false)
	private Integer stock;

	@Column(nullable = false)
	private Boolean isPublic;

	private String description;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "store_id", nullable = false)
	private Store store;

	public void softDelete(String userId) {
		this.deletedAt = LocalDateTime.now();
		this.deletedBy = userId;
	}

	public boolean isDeleted() {
		return this.deletedAt != null;
	}

}
