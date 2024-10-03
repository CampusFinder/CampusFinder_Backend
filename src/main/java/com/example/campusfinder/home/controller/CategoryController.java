package com.example.campusfinder.home.controller;

import com.example.campusfinder.core.base.BaseResponse;
import com.example.campusfinder.home.dto.CategoryResponseDto;
import com.example.campusfinder.home.service.CategoryService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * packageName    : com.example.campusfinder.home.controller
 * fileName       : CategoryController
 * author         : tlswl
 * date           : 2024-09-30
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-30        tlswl       최초 생성
 */
@RestController
@RequestMapping("/api/home")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/categories")
    public ResponseEntity<BaseResponse<List<CategoryResponseDto>>> getAllCategories(){
        List<CategoryResponseDto> categories = categoryService.getAllCategories();
        BaseResponse<List<CategoryResponseDto>> response = BaseResponse.ofSuccess(200,categories);
        return ResponseEntity.ok(response);
    }

}
