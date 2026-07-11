package com.streaming.admin.controller;

import com.streaming.admin.dto.request.AccountTypeRequestDto;
import com.streaming.admin.dto.response.AccountTypeResponseDto;
import com.streaming.admin.service.AccountTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/account-types")
@Tag(name = "AccountType", description = "CRUD operations for account types")
public class AccountTypeController {

    private final AccountTypeService service;

    public AccountTypeController(AccountTypeService service) {
        this.service = service;
    }

    @Operation(summary = "Get all account types")
    @GetMapping
    public List<AccountTypeResponseDto> findAll() {
        return service.findAll().stream()
                .map(AccountTypeResponseDto::fromEntity)
                .toList();
    }

    @Operation(summary = "Get account type by ID")
    @GetMapping("/{id}")
    public AccountTypeResponseDto findById(
            @Parameter(description = "ID of the account type", example = "1")
            @PathVariable Integer id
    ) {
        return AccountTypeResponseDto.fromEntity(service.findById(id));
    }

    @Operation(summary = "Create a new account type")
    @PostMapping
    public AccountTypeResponseDto create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Account type data",
                    required = true
            )
            @Valid @RequestBody AccountTypeRequestDto request
    ) {
        return AccountTypeResponseDto.fromEntity(service.create(request));
    }

    @Operation(summary = "Update an existing account type")
    @PutMapping("/{id}")
    public AccountTypeResponseDto update(
            @Parameter(description = "ID of the account type", example = "1")
            @PathVariable Integer id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated account type data",
                    required = true
            )
            @Valid @RequestBody AccountTypeRequestDto request
    ) {
        return AccountTypeResponseDto.fromEntity(service.update(id, request));
    }

    @Operation(summary = "Delete an account type")
    @DeleteMapping("/{id}")
    public void delete(
            @Parameter(description = "ID of the account type", example = "1")
            @PathVariable Integer id
    ) {
        service.delete(id);
    }
}
