package ru.practicum.category.service;

import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto addCategory(NewCategoryDto newCategoryDto);

    CategoryDto getCategoryById(Long categoryId);

    CategoryDto updateCategory(Long categoryId, NewCategoryDto newCategoryDto);

    List<CategoryDto> getCategories(int from, int size);

    void deleteCategory(Long categoryId);
}