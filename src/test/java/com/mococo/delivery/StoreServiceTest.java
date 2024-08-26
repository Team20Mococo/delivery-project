package com.mococo.delivery;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.mococo.delivery.application.dto.store.AddStoreResponseDto;
import com.mococo.delivery.application.dto.store.StoreRequestDto;
import com.mococo.delivery.application.service.StoreService;
import com.mococo.delivery.domain.model.Category;
import com.mococo.delivery.domain.model.Store;
import com.mococo.delivery.domain.model.User;
import com.mococo.delivery.domain.repository.CategoryRepository;
import com.mococo.delivery.domain.repository.StoreRepository;
import com.mococo.delivery.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;
import java.util.UUID;

public class StoreServiceTest {

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private StoreService storeService;

    private StoreRequestDto storeRequestDto;
    private User owner;
    private Category category;

    @BeforeEach
    void setUp() {
        storeRequestDto = StoreRequestDto.builder()
                .username("testuser")
                .name("Test Store")
                .category("KOREAN")
                .notice("Store Notice")
                .description("Store Description")
                .build();

        User owner = User.builder().username(storeRequestDto.getUsername()).build();
        Category category = Category.builder().name(storeRequestDto.getCategory()).build();
    }

    @Test
    void getStore_existingStore_returnStore() {
        UUID storeId = UUID.randomUUID();
        Store store = Store.builder()
                .id(storeId)
                .name("Test Store")
                .category(category)
                .owner(owner)
                .notice("Store Notice")
                .description("Store Description")
                .operationStatus(true)
                .build();

        when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));

        // when
        Store result = storeService.getStore(storeId);

        // then
        assertNotNull(result);
        assertEquals("Test Store", result.getName());
        verify(storeRepository, times(1)).findById(storeId);
    }

    @Test
    void getStore_nonExistingStore_returnsNull() {
        // given
        UUID storeId = UUID.randomUUID();

        when(storeRepository.findById(storeId)).thenReturn(Optional.empty());

        // when
        Store result = storeService.getStore(storeId);

        // then
        assertNull(result);
        verify(storeRepository, times(1)).findById(storeId);
    }

    @Test
    void addStore_success() {
        // given
        when(userRepository.findByUsername(storeRequestDto.getUsername())).thenReturn(Optional.of(owner));
        when(categoryRepository.findByName(storeRequestDto.getCategory())).thenReturn(Optional.of(category));
        when(storeRepository.save(any(Store.class))).thenAnswer(invocation -> {
            Store store = invocation.getArgument(0);
            return Store.builder()
                    .id(UUID.randomUUID())
                    .name(store.getName())
                    .category(store.getCategory())
                    .notice(store.getNotice())
                    .description(store.getDescription())
                    .operationStatus(store.getOperationStatus())
                    .owner(store.getOwner())
                    .build();
        });

        // when
        AddStoreResponseDto response = storeService.addStore(storeRequestDto);

        // then
        assertNotNull(response);
        assertNotNull(response.getStore().getStoreId());
        assertEquals(storeRequestDto.getName(), response.getStore().getName());
        verify(storeRepository, times(1)).save(any(Store.class));
    }

    @Test
    void addStore_invalidUser_throwsException() {
        // given
        when(userRepository.findByUsername(storeRequestDto.getUsername())).thenReturn(Optional.empty());

        // when / then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            storeService.addStore(storeRequestDto);
        });

        assertEquals("유효하지 않은 사용자 ID입니다.", exception.getMessage());
        verify(storeRepository, never()).save(any(Store.class));
    }

    @Test
    void addStore_invalidCategory_throwsException() {
        // given
        when(userRepository.findByUsername(storeRequestDto.getUsername())).thenReturn(Optional.of(owner));
        when(categoryRepository.findByName(storeRequestDto.getCategory())).thenReturn(Optional.empty());

        // when / then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            storeService.addStore(storeRequestDto);
        });

        assertEquals("유효하지 않은 카테고리입니다.", exception.getMessage());
        verify(storeRepository, never()).save(any(Store.class));
    }
}
