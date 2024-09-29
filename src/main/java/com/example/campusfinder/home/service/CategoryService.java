package com.example.campusfinder.home.service;

import com.example.campusfinder.home.dto.CategoryResponseDto;
import com.example.campusfinder.home.entity.CategoryType;
import com.example.campusfinder.home.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * packageName    : com.example.campusfinder.home.service
 * fileName       : CategoryService
 * author         : tlswl
 * date           : 2024-09-30
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-30        tlswl       최초 생성
 */
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<CategoryResponseDto> getAllCategories() {
        return Arrays.stream(CategoryType.values())
                .map(categoryType -> new CategoryResponseDto(categoryType, categoryType.getCategoryName()))
                .collect(Collectors.toList());
    }
}
