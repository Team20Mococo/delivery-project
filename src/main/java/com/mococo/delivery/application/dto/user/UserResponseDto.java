package com.mococo.delivery.application.dto.user;

import java.time.LocalDateTime;

import com.mococo.delivery.domain.model.User;
import com.mococo.delivery.domain.model.enumeration.UserRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDto {
	private String username;
	private String nickName;
	private String email;
	private UserRole role;
	private String address;
	private boolean isPublic;
	private LocalDateTime createdAt;
	private String createdBy;
	private LocalDateTime updatedAt;
	private String updatedBy;
	private LocalDateTime deletedAt;
	private String deletedBy;

	public static UserResponseDto of(User user) {
		return UserResponseDto.builder()
			.username(user.getUsername())
			.nickName(user.getNickname())
			.email(user.getEmail())
			.role(user.getRole())
			.address(user.getAddress())
			.isPublic(user.isPublic())
			.createdAt(user.getCreatedAt())
			.createdBy(user.getCreatedBy())
			.updatedAt(user.getUpdatedAt())
			.updatedBy(user.getUpdatedBy())
			.deletedAt(user.getDeletedAt())
			.deletedBy(user.getDeletedBy())
			.build();
	}
}
