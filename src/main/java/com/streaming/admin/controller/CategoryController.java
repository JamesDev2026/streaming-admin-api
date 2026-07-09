package com.streaming.admin.controller;

import com.streaming.admin.dto.request.CategoryRequestDto;
import com.streaming.admin.dto.response.CategoryResponseDto;
import com.streaming.admin.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "Category", description = "CRUD operations for categories")
public class CategoryController {

    private final CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }

    @Operation(summary = "Get all categories")
    @GetMapping
    public List<CategoryResponseDto> findAll() {
        return service.findAll().stream()
                .map(CategoryResponseDto::fromEntity)
                .toList();
    }

    @Operation(summary = "Get category by ID")
    @GetMapping("/{id}")
    public CategoryResponseDto findById(
            @Parameter(description = "ID of the category", example = "1")
            @PathVariable Integer id
    ) {
        return CategoryResponseDto.fromEntity(service.findById(id));
    }

    @Operation(summary = "Create a new category")
    @PostMapping
    public CategoryResponseDto create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Category data",
                    required = true
            )
            @Valid @RequestBody CategoryRequestDto request
    ) {
        return CategoryResponseDto.fromEntity(service.create(request));
    }

    @Operation(summary = "Update an existing category")
    @PutMapping("/{id}")
    public CategoryResponseDto update(
            @Parameter(description = "ID of the category", example = "1")
            @PathVariable Integer id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated category data",
                    required = true
            )
            @Valid @RequestBody CategoryRequestDto request
    ) {
        return CategoryResponseDto.fromEntity(service.update(id, request));
    }

    @Operation(summary = "Delete a category")
    @DeleteMapping("/{id}")
    public void delete(
            @Parameter(description = "ID of the category", example = "1")
            @PathVariable Integer id
    ) {
        service.delete(id);
    }
}
