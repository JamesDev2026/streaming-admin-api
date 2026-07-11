package com.streaming.admin.controller;

import com.streaming.admin.dto.request.PanelRequestDto;
import com.streaming.admin.dto.response.PanelResponseDto;
import com.streaming.admin.service.PanelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/panels")
@Tag(name = "Panel", description = "CRUD operations for panels")
public class PanelController {

    private final PanelService service;

    public PanelController(PanelService service) {
        this.service = service;
    }

    @Operation(summary = "Get all panels")
    @GetMapping
    public List<PanelResponseDto> findAll() {
        return service.findAll().stream()
                .map(PanelResponseDto::fromEntity)
                .toList();
    }

    @Operation(summary = "Get panel by ID")
    @GetMapping("/{id}")
    public PanelResponseDto findById(
            @Parameter(description = "ID of the panel", example = "1")
            @PathVariable Integer id
    ) {
        return PanelResponseDto.fromEntity(service.findById(id));
    }

    @Operation(summary = "Create a new panel")
    @PostMapping
    public PanelResponseDto create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Panel data",
                    required = true
            )
            @Valid @RequestBody PanelRequestDto request
    ) {
        return PanelResponseDto.fromEntity(service.create(request));
    }

    @Operation(summary = "Update an existing panel")
    @PutMapping("/{id}")
    public PanelResponseDto update(
            @Parameter(description = "ID of the panel", example = "1")
            @PathVariable Integer id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated panel data",
                    required = true
            )
            @Valid @RequestBody PanelRequestDto request
    ) {
        return PanelResponseDto.fromEntity(service.update(id, request));
    }

    @Operation(summary = "Delete a panel")
    @DeleteMapping("/{id}")
    public void delete(
            @Parameter(description = "ID of the panel", example = "1")
            @PathVariable Integer id
    ) {
        service.delete(id);
    }
}
