package com.mococo.delivery.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mococo.delivery.domain.model.OrderProduct;
import com.mococo.delivery.service.OrderProductService;

@RestController
@RequestMapping("/api/order-products")
public class OrderProductController {

	@Autowired
	private OrderProductService orderProductService;

	@GetMapping
	public ResponseEntity<List<OrderProduct>> getAllOrderProducts() {
		return new ResponseEntity<>(orderProductService.getAllOrderProducts(), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<OrderProduct> getOrderProductById(@PathVariable UUID id) {
		OrderProduct orderProduct = orderProductService.getOrderProductById(id);
		if (orderProduct != null) {
			return new ResponseEntity<>(orderProduct, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping
	public ResponseEntity<OrderProduct> createOrderProduct(@RequestBody OrderProduct orderProduct) {
		return new ResponseEntity<>(orderProductService.createOrderProduct(orderProduct), HttpStatus.CREATED);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteOrderProduct(@PathVariable UUID id) {
		orderProductService.deleteOrderProduct(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
