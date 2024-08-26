package com.mococo.delivery.application.service;

import com.mococo.delivery.application.dto.category.AddCategoryResponseDto;
import com.mococo.delivery.application.dto.category.CategoryRequestDto;
import com.mococo.delivery.application.dto.category.CategoryResponseDto;
import com.mococo.delivery.domain.model.Category;
import com.mococo.delivery.domain.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public AddCategoryResponseDto addCategory(CategoryRequestDto request) {
        if (categoryRepository.findByName(request.getName()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 카테고리입니다.");
        }

        Category category = Category.builder()
                .name(request.getName())
                .build();

        Category savedCategory = categoryRepository.save(category);

        // todo: createdBy 값 수정 필요
        CategoryResponseDto categoryResponseDto = CategoryResponseDto.builder()
                .name(savedCategory.getName())
                .createdBy(null)
                .createdAt(LocalDateTime.now())
                .build();

        return AddCategoryResponseDto.builder().category(categoryResponseDto).build();
    }
}
