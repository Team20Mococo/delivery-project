package com.mococo.delivery.domain.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mococo.delivery.domain.model.Order;

public interface OrderRepository extends JpaRepository<Order, UUID> {

}

