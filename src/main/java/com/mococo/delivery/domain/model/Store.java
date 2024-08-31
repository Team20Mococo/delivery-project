package com.mococo.delivery.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
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
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "p_stores")
@EntityListeners(AuditingEntityListener.class)
public class Store extends Auditable {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "store_id")
	private UUID id;

	@Column(nullable = false)
	private String name;

	private String notice;

	private String description;

	@Column(nullable = false)
	private Boolean operationStatus;

	@ManyToOne
	@JoinColumn(name = "owner_id", nullable = false)
	private User owner;

	@ManyToOne
	@JoinColumn(name = "category", nullable = false)
	private Category category;

	public void softDelete(String userId) {
		this.deletedAt = LocalDateTime.now();
		this.deletedBy = userId;
	}

	public boolean isDeleted() {
		return this.deletedAt != null;
	}

}
