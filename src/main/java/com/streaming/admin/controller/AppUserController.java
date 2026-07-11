package com.streaming.admin.controller;

import com.streaming.admin.dto.request.AppUserRequestDto;
import com.streaming.admin.dto.response.AppUserResponseDto;
import com.streaming.admin.service.AppUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/app-users")
@Tag(name = "AppUser", description = "CRUD operations for app users")
public class AppUserController {

    private final AppUserService service;

    public AppUserController(AppUserService service) {
        this.service = service;
    }

    @Operation(summary = "Get all app users")
    @GetMapping
    public List<AppUserResponseDto> findAll() {
        return service.findAll().stream()
                .map(AppUserResponseDto::fromEntity)
                .toList();
    }

    @Operation(summary = "Get app user by ID")
    @GetMapping("/{id}")
    public AppUserResponseDto findById(
            @Parameter(description = "ID of the app user", example = "1")
            @PathVariable Integer id
    ) {
        return AppUserResponseDto.fromEntity(service.findById(id));
    }

    @Operation(summary = "Create a new app user")
    @PostMapping
    public AppUserResponseDto create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "App user data",
                    required = true
            )
            @Valid @RequestBody AppUserRequestDto request
    ) {
        return AppUserResponseDto.fromEntity(service.create(request));
    }

    @Operation(summary = "Update an existing app user")
    @PutMapping("/{id}")
    public AppUserResponseDto update(
            @Parameter(description = "ID of the app user", example = "1")
            @PathVariable Integer id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated app user data",
                    required = true
            )
            @Valid @RequestBody AppUserRequestDto request
    ) {
        return AppUserResponseDto.fromEntity(service.update(id, request));
    }

    @Operation(summary = "Delete an app user")
    @DeleteMapping("/{id}")
    public void delete(
            @Parameter(description = "ID of the app user", example = "1")
            @PathVariable Integer id
    ) {
        service.delete(id);
    }
}
