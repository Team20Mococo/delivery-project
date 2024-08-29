package com.mococo.delivery.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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

	@ManyToOne
	@JoinColumn(name = "store_id", nullable = false)
	private Store store;

	public static class ProductBuilder {
		private LocalDateTime createdAt;
		private String createdBy;
		private LocalDateTime updatedAt;
		private String updatedBy;

		public ProductBuilder createdAt(LocalDateTime createdAt) {
			this.createdAt = createdAt;
			return this;
		}

		public ProductBuilder createdBy(String createdBy) {
			this.createdBy = createdBy;
			return this;
		}

		public ProductBuilder updatedAt(LocalDateTime updatedAt) {
			this.updatedAt = updatedAt;
			return this;
		}

		public ProductBuilder updatedBy(String updatedBy) {
			this.updatedBy = updatedBy;
			return this;
		}

		public Product build() {
			Product product = new Product();
			product.id = this.id;
			product.name = this.name;
			product.price = this.price;
			product.stock = this.stock;
			product.isPublic = this.isPublic;
			product.description = this.description;
			product.store = this.store;

			product.createdAt = this.createdAt;
			product.createdBy = this.createdBy;
			product.updatedAt = this.updatedAt;
			product.updatedBy = this.updatedBy;

			return product;
		}
	}
}
