package com.mococo.delivery.domain.model.enumeration;

import java.util.Arrays;

import com.mococo.delivery.domain.exception.enumtype.RoleTypeException;

import lombok.Getter;

@Getter
public enum UserRole {
	MASTER(Authority.MASTER),
	MANAGER(Authority.MANAGER),
	OWNER(Authority.OWNER),
	CUSTOMER(Authority.CUSTOMER);

	private final String authority;

	UserRole(String authority) {
		this.authority = authority;
	}

	public static class Authority {
		public static final String MASTER = "ROLE_MASER";
		public static final String MANAGER = "ROLE_MANAGER";
		public static final String OWNER = "ROLE_OWNER";
		public static final String CUSTOMER = "ROLE_CUSTOMER";
	}

	public static UserRole of(String authority) {
		return Arrays.stream(UserRole.values())
			.filter(userRole -> userRole.getAuthority().equals(authority))
			.findAny()
			.orElseThrow(RoleTypeException::new);
	}
}
