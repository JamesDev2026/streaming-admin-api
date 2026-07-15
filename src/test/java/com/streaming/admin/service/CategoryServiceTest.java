package com.streaming.admin.service;

import com.streaming.admin.dto.request.CategoryRequestDto;
import com.streaming.admin.entity.Category;
import com.streaming.admin.repository.AccountRepository;
import com.streaming.admin.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void findAll_returnsCategories() {
        when(categoryRepository.findAll()).thenReturn(List.of(
                Category.builder().idCategory(1).name("Netflix").build()
        ));

        List<Category> result = categoryService.findAll();

        assertEquals(1, result.size());
        assertEquals("Netflix", result.getFirst().getName());
    }

    @Test
    void findById_whenExists_returnsCategory() {
        Category category = Category.builder().idCategory(1).name("Netflix").build();
        when(categoryRepository.findById(1)).thenReturn(Optional.of(category));

        Category result = categoryService.findById(1);

        assertEquals(1, result.getIdCategory());
    }

    @Test
    void findById_whenMissing_throwsException() {
        when(categoryRepository.findById(99)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> categoryService.findById(99));
        assertEquals("Category not found", ex.getMessage());
    }

    @Test
    void create_savesCategory() {
        CategoryRequestDto request = CategoryRequestDto.builder()
                .name("Disney+")
                .description("Streaming Disney")
                .build();

        when(categoryRepository.save(any(Category.class))).thenAnswer(invocation -> {
            Category saved = invocation.getArgument(0);
            saved.setIdCategory(10);
            return saved;
        });

        Category result = categoryService.create(request);

        assertEquals(10, result.getIdCategory());
        assertEquals("Disney+", result.getName());
        assertEquals("Streaming Disney", result.getDescription());
    }

    @Test
    void update_modifiesExistingCategory() {
        Category existing = Category.builder().idCategory(1).name("Old").description("Old desc").build();
        when(categoryRepository.findById(1)).thenReturn(Optional.of(existing));
        when(categoryRepository.save(any(Category.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Category result = categoryService.update(1, CategoryRequestDto.builder()
                .name("New")
                .description("New desc")
                .build());

        assertEquals("New", result.getName());
        assertEquals("New desc", result.getDescription());
    }

    @Test
    void delete_whenLinkedToAccounts_throwsException() {
        when(categoryRepository.findById(1)).thenReturn(Optional.of(Category.builder().idCategory(1).build()));
        when(accountRepository.existsByIdCategory(1)).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> categoryService.delete(1));
        assertTrue(ex.getMessage().contains("linked to one or more accounts"));
        verify(categoryRepository, never()).deleteById(any());
    }

    @Test
    void delete_whenNotLinked_deletesCategory() {
        when(categoryRepository.findById(1)).thenReturn(Optional.of(Category.builder().idCategory(1).build()));
        when(accountRepository.existsByIdCategory(1)).thenReturn(false);

        categoryService.delete(1);

        verify(categoryRepository).deleteById(1);
    }
}
