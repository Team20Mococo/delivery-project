package com.mococo.delivery.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.mococo.delivery.domain.model.enumeration.UserRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "p_users")
public class User {

	@Id
	private String username;

	private String nickname;

	private String password;

	private String email;

	@Enumerated(value = EnumType.STRING)
	private UserRole role;

	private String address;

	private boolean isPublic;

}
