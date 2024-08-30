package com.mococo.delivery;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mococo.delivery.application.dto.user.LoginRequestDto;
import com.mococo.delivery.application.dto.user.SignUpRequestDto;
import com.mococo.delivery.application.dto.user.UserPutRequestDto;
import com.mococo.delivery.application.dto.user.UserResponseDto;
import com.mococo.delivery.application.dto.user.UserRolePatchRequestDto;
import com.mococo.delivery.application.service.UserService;
import com.mococo.delivery.domain.exception.entity.PasswordNotMatchedException;
import com.mococo.delivery.domain.exception.entity.UserNotFoundException;
import com.mococo.delivery.domain.model.User;
import com.mococo.delivery.domain.model.enumeration.UserRole;
import com.mococo.delivery.domain.repository.UserRepository;

class UserServiceTest {

	@InjectMocks
	private UserService userService;

	@Mock
	private UserRepository userRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private HttpServletResponse response;

	private ObjectMapper objectMapper;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		objectMapper = new ObjectMapper();
	}

	@Test
	void testGetUser_UserExists() {
		// Given
		String username = "testUser";
		User user = User.builder()
			.username(username)
			.nickname("testNickname")
			.email("test@example.com")
			.address("Test Address")
			.role(UserRole.ROLE_CUSTOMER)
			.build();

		when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

		// When
		UserResponseDto result = userService.getUser(username);

		// Then
		assertNotNull(result);
		assertEquals(username, result.getUsername());
		verify(userRepository).findByUsername(username);
	}

	@Test
	void testGetUser_UserNotFound() {
		// Given
		String username = "testUser";
		when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

		// When / Then
		assertThrows(EntityNotFoundException.class, () -> userService.getUser(username));
		verify(userRepository).findByUsername(username);
	}

	// Get All Users 는 Postman 으로 대체

	@Test
	void testUpdateUser_UserExists() throws Exception {
		// Given
		String username = "testUser";
		String jsonRequest = "{\"nickname\":\"newNickname\",\"address\":\"newAddress\",\"public\":true}";
		UserPutRequestDto requestDto = objectMapper.readValue(jsonRequest, UserPutRequestDto.class);

		User user = User.builder()
			.username(username)
			.nickname("oldNickname")
			.email("test@example.com")
			.address("Old Address")
			.role(UserRole.ROLE_CUSTOMER)
			.build();

		when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

		// When
		UserResponseDto result = userService.updateUser(username, requestDto);

		// Then
		assertNotNull(result);
		assertEquals(requestDto.getNickname(), result.getNickName());
		verify(userRepository).findByUsername(username);
	}

	@Test
	void testUpdateUser_UserNotFound() throws Exception {
		// Given
		String username = "testUser";
		String jsonRequest = "{\"nickname\":\"newNickname\",\"address\":\"newAddress\",\"public\":true}";
		UserPutRequestDto requestDto = objectMapper.readValue(jsonRequest, UserPutRequestDto.class);

		when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

		// When / Then
		assertThrows(UserNotFoundException.class, () -> userService.updateUser(username, requestDto));
		verify(userRepository).findByUsername(username);
	}

	@Test
	void testUpdateRole_UserExists() throws Exception {
		// Given
		String username = "testUser";
		String jsonRequest = "{\"roleName\":\"ROLE_OWNER\"}";
		UserRolePatchRequestDto requestDto = objectMapper.readValue(jsonRequest, UserRolePatchRequestDto.class);

		User user = User.builder()
			.username(username)
			.nickname("testNickname")
			.email("test@example.com")
			.address("Test Address")
			.role(UserRole.ROLE_CUSTOMER)
			.build();

		when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

		// When
		UserResponseDto result = userService.updateRole(username, requestDto);

		// Then
		assertNotNull(result);
		assertEquals(UserRole.ROLE_OWNER, result.getRole());
		verify(userRepository).findByUsername(username);
	}

	@Test
	void testUpdateRole_UserNotFound() throws Exception {
		// Given
		String username = "testUser";
		String jsonRequest = "{\"roleName\":\"ROLE_ADMIN\"}";
		UserRolePatchRequestDto requestDto = objectMapper.readValue(jsonRequest, UserRolePatchRequestDto.class);

		when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

		// When / Then
		assertThrows(UserNotFoundException.class, () -> userService.updateRole(username, requestDto));
		verify(userRepository).findByUsername(username);
	}

	@Test
	void testDeleteUser_UserExists() {
		// Given
		String username = "testUser";
		User user = User.builder()
			.username(username)
			.build();

		when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
		when(userRepository.delete(user)).thenReturn(true);

		// When
		Boolean result = userService.deleteUser(username);

		// Then
		assertTrue(result);
		verify(userRepository).findByUsername(username);
		verify(userRepository).delete(user);
	}

	@Test
	void testDeleteUser_UserNotFound() {
		// Given
		String username = "testUser";
		when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

		// When / Then
		assertThrows(UserNotFoundException.class, () -> userService.deleteUser(username));
		verify(userRepository).findByUsername(username);
	}

	@Test
	void testSignUp() throws Exception {
		// Given
		String jsonRequest = "{\"username\":\"testUser\",\"nickname\":\"testNickname\",\"email\":\"test@example.com\",\"address\":\"Test Address\",\"password\":\"password123\"}";
		SignUpRequestDto requestDto = objectMapper.readValue(jsonRequest, SignUpRequestDto.class);

		User user = User.builder()
			.username(requestDto.getUsername())
			.nickname(requestDto.getNickname())
			.email(requestDto.getEmail())
			.address(requestDto.getAddress())
			.password(passwordEncoder.encode(requestDto.getPassword()))
			.role(UserRole.ROLE_CUSTOMER)
			.build();

		when(userRepository.save(any(User.class))).thenReturn(Optional.of(user));

		// When
		Boolean result = userService.signUp(requestDto);

		// Then
		assertTrue(result);
		verify(userRepository).save(any(User.class));
	}

	// login test Postman 으로 대체

	@Test
	void testLogin_UserNotFound() throws Exception {
		// Given
		String jsonRequest = "{\"username\":\"testUser\",\"password\":\"testPassword\"}";
		LoginRequestDto requestDto = objectMapper.readValue(jsonRequest, LoginRequestDto.class);

		when(userRepository.findByUsername(requestDto.getUsername())).thenReturn(Optional.empty());

		// When / Then
		assertThrows(UserNotFoundException.class, () -> userService.login(response, requestDto));
		verify(userRepository).findByUsername(requestDto.getUsername());
	}

	@Test
	void testLogin_PasswordNotMatched() throws Exception {
		// Given
		String jsonRequest = "{\"username\":\"testUser\",\"password\":\"testPassword\"}";
		LoginRequestDto requestDto = objectMapper.readValue(jsonRequest, LoginRequestDto.class);

		User user = User.builder()
			.username(requestDto.getUsername())
			.password("encodedPassword")
			.nickname("testNickname")
			.email("test@example.com")
			.address("Test Address")
			.role(UserRole.ROLE_CUSTOMER)
			.build();

		when(userRepository.findByUsername(requestDto.getUsername())).thenReturn(Optional.of(user));
		when(passwordEncoder.matches(requestDto.getPassword(), user.getPassword())).thenReturn(false);

		// When / Then
		assertThrows(PasswordNotMatchedException.class, () -> userService.login(response, requestDto));
		verify(userRepository).findByUsername(requestDto.getUsername());
		verify(passwordEncoder).matches(requestDto.getPassword(), user.getPassword());
	}
}
