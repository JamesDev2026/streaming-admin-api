package com.streaming.admin.controller;

import com.streaming.admin.dto.request.AccountSubscriptionRequestDto;
import com.streaming.admin.dto.request.ChangeAccountRequestDto;
import com.streaming.admin.dto.request.RenewSubscriptionRequestDto;
import com.streaming.admin.dto.request.UpdatePaymentStatusRequestDto;
import com.streaming.admin.dto.response.AccountSubscriptionResponseDto;
import com.streaming.admin.dto.response.CountActiveUsersResponseDto;
import com.streaming.admin.dto.response.DeactivateSubscriptionResponseDto;
import com.streaming.admin.service.AccountSubscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/subscriptions")
@Tag(name = "Account Subscription", description = "CRUD and assignment operations between accounts and clients")
public class AccountSubscriptionController {

    private final AccountSubscriptionService service;

    public AccountSubscriptionController(AccountSubscriptionService service) {
        this.service = service;
    }

    @Operation(summary = "Get all subscriptions")
    @GetMapping
    public List<AccountSubscriptionResponseDto> findAll() {
        return service.findAll().stream()
                .map(AccountSubscriptionResponseDto::fromEntity)
                .toList();
    }

    @Operation(summary = "Get all active subscriptions")
    @GetMapping("/active")
    public List<AccountSubscriptionResponseDto> findActive() {
        return service.findActive().stream()
                .map(AccountSubscriptionResponseDto::fromEntity)
                .toList();
    }

    @Operation(summary = "Get subscription by ID")
    @GetMapping("/{id}")
    public AccountSubscriptionResponseDto findById(
            @Parameter(description = "ID of the subscription", example = "5")
            @PathVariable Integer id
    ) {
        return AccountSubscriptionResponseDto.fromEntity(service.findById(id));
    }

    @Operation(summary = "Get active subscription by account and user")
    @GetMapping("/by-account-user")
    public Optional<AccountSubscriptionResponseDto> getActiveSubscription(
            @Parameter(description = "ID de la cuenta", example = "1")
            @RequestParam Integer idAccount,
            @Parameter(description = "ID del usuario", example = "10")
            @RequestParam Integer idAppUser
    ) {
        return service.getActiveSubscription(idAccount, idAppUser)
                .map(AccountSubscriptionResponseDto::fromEntity);
    }

    @Operation(summary = "Get active subscriptions by account")
    @GetMapping("/by-account/{idAccount}")
    public List<AccountSubscriptionResponseDto> findByAccount(
            @Parameter(description = "ID de la cuenta", example = "1")
            @PathVariable Integer idAccount
    ) {
        return service.findActiveByAccount(idAccount).stream()
                .map(AccountSubscriptionResponseDto::fromEntity)
                .toList();
    }

    @Operation(summary = "Get subscription history by client")
    @GetMapping("/by-user/{idAppUser}")
    public List<AccountSubscriptionResponseDto> findByAppUser(
            @Parameter(description = "ID del cliente", example = "10")
            @PathVariable Integer idAppUser
    ) {
        return service.findByAppUser(idAppUser).stream()
                .map(AccountSubscriptionResponseDto::fromEntity)
                .toList();
    }

    @Operation(summary = "Get active subscriptions with pending payment")
    @GetMapping("/pending-payments")
    public List<AccountSubscriptionResponseDto> findPendingPayments() {
        return service.findPendingPayments().stream()
                .map(AccountSubscriptionResponseDto::fromEntity)
                .toList();
    }

    @Operation(summary = "Count active users and available slots in an account")
    @GetMapping("/count-active")
    public CountActiveUsersResponseDto countActiveUsers(
            @Parameter(description = "ID de la cuenta", example = "1")
            @RequestParam Integer idAccount
    ) {
        return service.countActiveUsers(idAccount);
    }

    @Operation(summary = "Assign a client to an account")
    @PostMapping
    public AccountSubscriptionResponseDto create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Subscription/assignment data (without id)",
                    required = true
            )
            @Valid @RequestBody AccountSubscriptionRequestDto request
    ) {
        return AccountSubscriptionResponseDto.fromEntity(service.create(request));
    }

    @Operation(summary = "Update a subscription")
    @PutMapping("/{id}")
    public AccountSubscriptionResponseDto update(
            @Parameter(description = "ID of the subscription", example = "5")
            @PathVariable Integer id,
            @Valid @RequestBody AccountSubscriptionRequestDto request
    ) {
        return AccountSubscriptionResponseDto.fromEntity(service.update(id, request));
    }

    @Operation(summary = "Change the account assigned to a subscription")
    @PutMapping("/{id}/change-account")
    public AccountSubscriptionResponseDto changeAccount(
            @Parameter(description = "ID of the subscription", example = "5")
            @PathVariable Integer id,
            @Valid @RequestBody ChangeAccountRequestDto request
    ) {
        return AccountSubscriptionResponseDto.fromEntity(service.changeAccount(id, request));
    }

    @Operation(summary = "Update payment status (Pagado / Pendiente)")
    @PutMapping("/{id}/payment-status")
    public AccountSubscriptionResponseDto updatePaymentStatus(
            @Parameter(description = "ID of the subscription", example = "5")
            @PathVariable Integer id,
            @Valid @RequestBody UpdatePaymentStatusRequestDto request
    ) {
        return AccountSubscriptionResponseDto.fromEntity(service.updatePaymentStatus(id, request));
    }

    @Operation(summary = "Deactivate a subscription (soft delete)")
    @PutMapping("/{id}/deactivate")
    public DeactivateSubscriptionResponseDto deactivate(
            @Parameter(description = "ID of the subscription", example = "5")
            @PathVariable Integer id
    ) {
        return DeactivateSubscriptionResponseDto.fromEntity(service.deactivate(id));
    }

    @Operation(summary = "Renew a subscription: deactivate current period and create a new active one (keeps history)")
    @PostMapping("/{id}/renew")
    public AccountSubscriptionResponseDto renew(
            @Parameter(description = "ID of the active subscription to renew", example = "5")
            @PathVariable Integer id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "New subscription period data (client is taken from the current subscription)",
                    required = true
            )
            @Valid @RequestBody RenewSubscriptionRequestDto request
    ) {
        return AccountSubscriptionResponseDto.fromEntity(service.renew(id, request));
    }

    @Operation(summary = "Delete a subscription permanently")
    @DeleteMapping("/{id}")
    public void delete(
            @Parameter(description = "ID of the subscription", example = "5")
            @PathVariable Integer id
    ) {
        service.delete(id);
    }
}
