package com.mococo.delivery.adapters.controller;

import com.mococo.delivery.application.dto.SuccessResponseDto;
import com.mococo.delivery.application.dto.category.AddCategoryResponseDto;
import com.mococo.delivery.application.dto.category.CategoryRequestDto;
import com.mococo.delivery.application.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("")
    public SuccessResponseDto<AddCategoryResponseDto> addCategory(@RequestBody CategoryRequestDto request) {
        AddCategoryResponseDto response = categoryService.addCategory(request);
        return new SuccessResponseDto<>("카테고리 추가에 성공했습니다.", response);
    }
}
