package com.streaming.admin.service;

import com.streaming.admin.dto.request.CategoryRequestDto;
import com.streaming.admin.entity.Category;
import com.streaming.admin.repository.AccountRepository;
import com.streaming.admin.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final AccountRepository accountRepository;

    public CategoryService(CategoryRepository categoryRepository, AccountRepository accountRepository) {
        this.categoryRepository = categoryRepository;
        this.accountRepository = accountRepository;
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Category findById(Integer id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    public Category create(CategoryRequestDto request) {
        Category category = Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();

        return categoryRepository.save(category);
    }

    public Category update(Integer id, CategoryRequestDto request) {
        Category category = findById(id);
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        return categoryRepository.save(category);
    }

    public void delete(Integer id) {
        findById(id);

        if (accountRepository.existsByIdCategory(id)) {
            throw new RuntimeException("Cannot delete category because it is linked to one or more accounts");
        }

        categoryRepository.deleteById(id);
    }
}
