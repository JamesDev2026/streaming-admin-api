package com.streaming.admin.controller;

import com.streaming.admin.dto.request.AccountRequestDto;
import com.streaming.admin.dto.response.AccountResponseDto;
import com.streaming.admin.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@Tag(name = "Account", description = "CRUD operations for accounts")
public class AccountController {

    private final AccountService service;

    public AccountController(AccountService service) {
        this.service = service;
    }

    @Operation(summary = "Get all accounts")
    @GetMapping
    public List<AccountResponseDto> findAll() {
        return service.findAll().stream()
                .map(AccountResponseDto::fromEntity)
                .toList();
    }

    @Operation(summary = "Get account by ID")
    @GetMapping("/{id}")
    public AccountResponseDto findById(
            @Parameter(description = "ID of the account", example = "1")
            @PathVariable Integer id
    ) {
        return AccountResponseDto.fromEntity(service.findById(id));
    }

    @Operation(summary = "Create a new account")
    @PostMapping
    public AccountResponseDto create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Account data",
                    required = true
            )
            @Valid @RequestBody AccountRequestDto request
    ) {
        return AccountResponseDto.fromEntity(service.create(request));
    }

    @Operation(summary = "Update an existing account")
    @PutMapping("/{id}")
    public AccountResponseDto update(
            @Parameter(description = "ID of the account", example = "1")
            @PathVariable Integer id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated account data",
                    required = true
            )
            @Valid @RequestBody AccountRequestDto request
    ) {
        return AccountResponseDto.fromEntity(service.update(id, request));
    }

    @Operation(summary = "Delete an account")
    @DeleteMapping("/{id}")
    public void delete(
            @Parameter(description = "ID of the account", example = "1")
            @PathVariable Integer id
    ) {
        service.delete(id);
    }
}
