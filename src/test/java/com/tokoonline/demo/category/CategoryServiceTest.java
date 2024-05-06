package com.tokoonline.demo.category;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.tokoonline.demo.category.model.Category;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    
    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Test
    void fetchAllById_shouldReturnListCategory_whenCategoryIsNotEmpty(){
        Category firstCategory = Category.builder()
            .id(UUID.randomUUID())
            .name("Electronics")
            .description("Products such as smartphones, laptops, TVs, and cameras that utilize electronic technology for various purposes, including communication, entertainment, and productivity.")
            .build();
            Category secondCategory = Category.builder()
            .id(UUID.randomUUID())
            .name("Health & Beauty")
            .description("Products aimed at enhancing personal well-being and appearance, including skincare, haircare, cosmetics, vitamins, and supplements.")
            .build();
        List<UUID> categories = List.of(firstCategory.getId(), secondCategory.getId());
        List<Category> expectedResult = List.of(firstCategory, secondCategory);
        Mockito.when(categoryRepository.findAllById(categories)).thenReturn(expectedResult);

        List<Category> actualResult = categoryService.fetchAllById(categories);

        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    void fetchAllById_shouldThrowErrorException_whenGivenCategoryIsNotFound(){
        List<UUID> id = List.of(UUID.randomUUID());
        Mockito.when(categoryRepository.findAllById(id)).thenReturn(new ArrayList<>());

        Assertions.assertThrows(CategoryNotFoundException.class, () -> categoryService.fetchAllById(id));
    }
}
