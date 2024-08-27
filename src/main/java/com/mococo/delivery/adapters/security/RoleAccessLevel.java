package com.mococo.delivery.adapters.security;

import static org.springframework.http.HttpMethod.*;

import java.util.Arrays;
import java.util.List;

import com.mococo.delivery.domain.exception.enumtype.RoleTypeException;
import com.mococo.delivery.domain.model.enumeration.UserRole;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RoleAccessLevel {
	MASTER(UserRole.MASTER, List.of(
		// 유저
		EndPoint.of(GET, "/api/v1/admin/users"),
		EndPoint.of(GET, "/api/v1/admin/users/**"),
		// 상품
		EndPoint.of(GET, "/api/v1/products"),
		EndPoint.of(GET, "/api/v1/products/**"),
		EndPoint.of(PUT, "/api/v1/products/**"),
		EndPoint.of(DELETE, "/api/v1/products/**"),
		// order
		EndPoint.of(GET, "/api/v1/orders/**"),
		EndPoint.of(GET, "/api/v1/owner/orders"),
		EndPoint.of(GET, "/api/v1/consumer/orders"),
		EndPoint.of(PATCH, "/api/v1/orders/**"),
		EndPoint.of(DELETE, "/api/v1/orders/**"),
		// payment
		EndPoint.of(GET, "/api/v1/payments"),
		// delivery
		EndPoint.of(GET, "/api/v1/deliveries"),
		// store
		EndPoint.of(GET, "/api/v1/admin/stores"),
		EndPoint.of(GET, "/api/v1/admin/stores/**"),
		EndPoint.of(POST, "/api/v1/admin/stores"),
		//category
		EndPoint.of(POST, "/api/v1/admin/categories"),
		EndPoint.of(GET, "/api/v1/admin/categories"),
		EndPoint.of(PUT, "/api/v1/admin/categories/**"),
		EndPoint.of(DELETE, "/api/v1/admin/categories/**"),
		//ai
		EndPoint.of(GET, "/api/v1/admin/ai")
	)),
	MANAGER(UserRole.MANAGER, List.of(
		// 유저
		EndPoint.of(GET, "/api/v1/admin/users"),
		EndPoint.of(GET, "/api/v1/admin/users/**"),
		// 상품
		EndPoint.of(GET, "/api/v1/products"),
		EndPoint.of(GET, "/api/v1/products/**"),
		EndPoint.of(PUT, "/api/v1/products/**"),
		EndPoint.of(DELETE, "/api/v1/products/**"),
		// order
		EndPoint.of(GET, "/api/v1/orders/**"),
		EndPoint.of(GET, "/api/v1/owner/orders"),
		EndPoint.of(GET, "/api/v1/consumer/orders"),
		EndPoint.of(PATCH, "/api/v1/orders/**"),
		EndPoint.of(DELETE, "/api/v1/orders/**"),
		// payment
		EndPoint.of(GET, "/api/v1/payments"),
		// delivery
		EndPoint.of(GET, "/api/v1/deliveries"),
		// store
		EndPoint.of(GET, "/api/v1/admin/stores"),
		EndPoint.of(GET, "/api/v1/admin/stores/**"),
		EndPoint.of(POST, "/api/v1/admin/stores"),
		//category
		EndPoint.of(POST, "/api/v1/admin/categories"),
		EndPoint.of(GET, "/api/v1/admin/categories"),
		EndPoint.of(PUT, "/api/v1/admin/categories/**"),
		EndPoint.of(DELETE, "/api/v1/admin/categories/**"),
		//ai
		EndPoint.of(GET, "/api/v1/admin/ai")
	)),
	OWNER(UserRole.OWNER, List.of(
		// 유저
		EndPoint.of(POST, "/api/v1/users/log-out"),
		EndPoint.of(PUT, "/api/v1/users/**"),
		EndPoint.of(DELETE, "/api/v1/users/**"),
		// 상품
		EndPoint.of(POST, "/api/v1/products"),
		EndPoint.of(GET, "/api/v1/products"),
		EndPoint.of(GET, "/api/v1/products/**"),
		EndPoint.of(PUT, "/api/v1/products/**"),
		EndPoint.of(DELETE, "/api/v1/products/**"),
		// order
		EndPoint.of(GET, "/api/v1/orders/**"),
		EndPoint.of(GET, "/api/v1/owner/orders"),
		EndPoint.of(PATCH, "/api/v1/orders/**"),
		EndPoint.of(DELETE, "/api/v1/orders/**"),
		// payment
		EndPoint.of(POST, "/api/v1/payments"),
		EndPoint.of(GET, "/api/v1/payments"),
		EndPoint.of(GET, "/api/v1/payments/**"),
		EndPoint.of(PUT, "/api/v1/payments/**"),
		EndPoint.of(DELETE, "/api/v1/payments/**"),
		// delivery
		EndPoint.of(POST, "/api/v1/deliveries"),
		EndPoint.of(GET, "/api/v1/deliveries"),
		EndPoint.of(PUT, "/api/v1/deliveries/**"),
		EndPoint.of(DELETE, "/api/v1/deliveries/**"),
		// store
		EndPoint.of(GET, "/api/v1/owner/stores/**"),
		EndPoint.of(PUT, "/api/v1/owner/stores/**"),
		EndPoint.of(PATCH, "/api/v1/owner/stores/**"),
		EndPoint.of(DELETE, "/api/v1/owner/stores/**"),
		//category
		//ai
		EndPoint.of(POST, "/api/v1/owner/ai")
	)),
	CUSTOMER(UserRole.CUSTOMER, List.of(
		// 유저
		EndPoint.of(POST, "/api/v1/users/log-out"),
		EndPoint.of(PUT, "/api/v1/users/**"),
		EndPoint.of(DELETE, "/api/v1/users/**"),
		// 상품
		EndPoint.of(GET, "/api/v1/products"),
		EndPoint.of(GET, "/api/v1/products/**"),
		// order
		EndPoint.of(POST, "/api/v1/orders"),
		EndPoint.of(GET, "/api/v1/orders/**"),
		EndPoint.of(GET, "/api/v1/consumer/orders"),
		EndPoint.of(DELETE, "/api/v1/orders/**"),
		// payment
		EndPoint.of(GET, "/api/v1/payments"),
		// delivery
		EndPoint.of(GET, "/api/v1/deliveries"),
		// store
		EndPoint.of(GET, "/api/v1/customer/stores"),
		EndPoint.of(GET, "/api/v1/customer/stores/**")
		//category
		//ai
	));
	private final UserRole role;
	private final List<EndPoint> endpointList;

	public static RoleAccessLevel of(UserRole role) {
		return Arrays.stream(RoleAccessLevel.values())
			.filter(accessLevel -> accessLevel.getRole().equals(role))
			.findAny()
			.orElseThrow(RoleTypeException::new);
	}
}
