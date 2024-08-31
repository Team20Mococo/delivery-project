package com.mococo.delivery.application.dto.user;

import java.util.List;

import com.mococo.delivery.application.dto.PageInfoDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserListResponseDto {
	private List<UserResponseDto> userList;
	PageInfoDto pageInfo;

	public static UserListResponseDto of(List<UserResponseDto> userResponseList, PageInfoDto pageInfo) {
		return UserListResponseDto.builder()
			.userList(userResponseList)
			.pageInfo(pageInfo)
			.build();
	}
}
