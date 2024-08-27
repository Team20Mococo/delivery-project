package com.mococo.delivery;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.mococo.delivery.application.dto.category.AddCategoryResponseDto;
import com.mococo.delivery.application.dto.category.CategoryRequestDto;
import com.mococo.delivery.application.service.CategoryService;
import com.mococo.delivery.domain.model.Category;
import com.mococo.delivery.domain.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private CategoryRequestDto categoryRequestDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        categoryRequestDto = CategoryRequestDto.builder()
                .name("TEXMEX")
                .build();
    }

    @Test
    void addCategory_success() {
        // given
        when(categoryRepository.findByName(categoryRequestDto.getName())).thenReturn(Optional.empty());
        when(categoryRepository.save(any(Category.class))).thenAnswer(invocation -> {
            Category category = invocation.getArgument(0);
            return Category.builder()
                    .name(category.getName())
                    .build();
        });

        // when
        AddCategoryResponseDto response = categoryService.addCategory(categoryRequestDto);

        // then
        assertNotNull(response);
        assertNotNull(response.getCategory());
        assertEquals("TEXMEX", response.getCategory().getName());
        assertNull(response.getCategory().getCreatedBy());
        assertNotNull(response.getCategory().getCreatedAt());

        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void addCategory_alreadyExists_throwsException() {
        // given
        when(categoryRepository.findByName(categoryRequestDto.getName())).thenReturn(Optional.of(new Category()));

        // when / then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            categoryService.addCategory(categoryRequestDto);
        });

        assertEquals("이미 존재하는 카테고리입니다.", exception.getMessage());
        verify(categoryRepository, never()).save(any(Category.class));
    }

}
