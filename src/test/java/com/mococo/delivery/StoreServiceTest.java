package com.mococo.delivery;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.mococo.delivery.application.service.StoreService;
import com.mococo.delivery.domain.model.Store;
import com.mococo.delivery.domain.repository.StoreRepository;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;
import java.util.UUID;

public class StoreServiceTest {

    @Mock
    private StoreRepository storeRepository;

    @InjectMocks
    private StoreService storeService;


    @Test
    void getStore_existingStore_returnStore() {
        UUID storeId = UUID.randomUUID();
        Store store = Store.builder().id(storeId).name("Test Store").build();

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
}
