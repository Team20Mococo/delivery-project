package com.mococo.delivery.domain.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mococo.delivery.domain.model.Delivery;

public interface DeliveryRepository extends JpaRepository<Delivery, UUID> {
	// 필요한 경우 커스텀 쿼리를 작성할 수 있습니다.
}
