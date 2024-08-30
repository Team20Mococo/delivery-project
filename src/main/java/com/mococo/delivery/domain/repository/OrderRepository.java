package com.mococo.delivery.domain.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.mococo.delivery.application.dto.order.OrderSearchCriteriaDto;
import com.mococo.delivery.domain.model.Order;

public interface OrderRepository extends JpaRepository<Order, UUID> {
	Optional<Order> findById(UUID orderId);

	@Query("SELECT o FROM Order o WHERE " +
		"(:#{#searchCriteria.username} IS NULL OR o.user.username = :#{#searchCriteria.username}) AND " +
		"(:#{#searchCriteria.storeName} IS NULL OR o.store.name = :#{#searchCriteria.storeName}) AND " +
		"(:#{#searchCriteria.status} IS NULL OR o.orderStatus = :#{#searchCriteria.status}) AND " +
		"(:#{#searchCriteria.type} IS NULL OR o.type = :#{#searchCriteria.type})")
	Page<Order> searchOrders(OrderSearchCriteriaDto searchCriteria, Pageable pageable);
}
