// package com.mococo.delivery;
//
// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.Mockito.*;
//
// import java.util.Optional;
//
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.MockitoAnnotations;
//
// import com.mococo.delivery.application.service.UserService;
// import com.mococo.delivery.domain.model.User;
// import com.mococo.delivery.domain.repository.UserRepository;
//
// public class UserServiceTest {
//
// 	@Mock
// 	private UserRepository userRepository;
//
// 	@InjectMocks
// 	private UserService userService;
//
// 	@BeforeEach
// 	void setUp() {
// 		MockitoAnnotations.openMocks(this);
// 	}
//
// 	@Test
// 	void testGetUser_UserExists() {
// 		// Given
// 		String username = "testuser";
// 		User mockUser = User.builder().username(username).build();
//
// 		when(userRepository.findByUsername(username)).thenReturn(Optional.of(mockUser));
//
// 		// When
// 		User result = userService.getUser(username);
//
// 		// Then
// 		assertNotNull(result);
// 		assertEquals(username, result.getUsername());
// 		verify(userRepository, times(1)).findByUsername(username);
// 	}
//
// 	@Test
// 	void testGetUser_UserDoesNotExist() {
// 		// Given
// 		String username = "nonexistentuser";
//
// 		when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
//
// 		// When
// 		User result = userService.getUser(username);
//
// 		// Then
// 		assertNull(result);
// 		verify(userRepository, times(1)).findByUsername(username);
// 	}
//
// 	@Test
// 	void testSaveUsers() {
// 		// Given
// 		for (int i = 1; i <= 10; i++) {
// 			String username = "test" + String.format("%02d", i);
// 			User mockUser = User.builder().username(username).build();
//
// 			when(userRepository.save(any(User.class))).thenReturn(Optional.of(mockUser));
//
// 			// When
// 			User result = userService.save(username);
//
// 			// Then
// 			assertNotNull(result);
// 			assertEquals(username, result.getUsername());
// 			verify(userRepository, times(1)).save(any(User.class));
//
// 			// 목 데이터 리셋
// 			reset(userRepository);
// 		}
// 	}
// }
