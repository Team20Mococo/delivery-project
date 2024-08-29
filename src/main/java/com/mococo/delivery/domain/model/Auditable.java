package com.mococo.delivery.domain.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable {

	@CreatedDate
	@Column(name = "created_at", updatable = false)
	protected LocalDateTime createdAt;

	@CreatedBy
	@Column(name = "created_by", updatable = false)
	protected String createdBy;

	@LastModifiedDate
	@Column(name = "updated_at")
	protected LocalDateTime updatedAt;

	@LastModifiedBy
	@Column(name = "updated_by")
	protected String updatedBy;

	@Column(name = "deleted_at")
	protected LocalDateTime deletedAt;

	@Column(name = "deleted_by")
	protected String deletedBy;
}