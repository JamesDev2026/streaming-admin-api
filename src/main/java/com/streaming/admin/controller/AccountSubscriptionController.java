package com.streaming.admin.controller;

import com.streaming.admin.dto.response.AccountSubscriptionResponseDto;
import com.streaming.admin.dto.response.CountActiveUsersResponseDto;
import com.streaming.admin.dto.response.DeactivateSubscriptionResponseDto;
import com.streaming.admin.entity.AccountSubscription;
import com.streaming.admin.service.AccountSubscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/subscriptions")
@Tag(name = "Account Subscription", description = "Operations about subscriptions of accounts")
public class AccountSubscriptionController {

    private final AccountSubscriptionService service;

    public AccountSubscriptionController(AccountSubscriptionService service) {
        this.service = service;
    }

    @Operation(summary = "Get active subscription by account and user")
    @GetMapping("/active")
    public Optional<AccountSubscriptionResponseDto> getActiveSubscription(
            @Parameter(description = "ID de la cuenta", example = "1")
            @RequestParam Integer idAccount,

            @Parameter(description = "ID del usuario", example = "10")
            @RequestParam Integer idAppUser
    ) {
        return service.getActiveSubscription(idAccount, idAppUser)
                .map(AccountSubscriptionResponseDto::fromEntity);
    }

    @Operation(summary = "Count active users in an account")
    @GetMapping("/count-active")
    public CountActiveUsersResponseDto countActiveUsers(
            @Parameter(description = "ID de la cuenta", example = "1")
            @RequestParam Integer idAccount
    ) {
        return CountActiveUsersResponseDto.builder()
                .idAccount(idAccount)
                .activeUsersCount(service.countActiveUsers(idAccount))
                .build();
    }

    @Operation(summary = "Create a new subscription")
    @PostMapping
    public AccountSubscriptionResponseDto createSubscription(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Object of subscription",
                    required = true
            )
            @RequestBody AccountSubscription subscription
    ) {
        return AccountSubscriptionResponseDto.fromEntity(service.save(subscription));
    }

    @Operation(summary = "Deactivate subscription by ID")
    @PutMapping("/deactivate/{id}")
    public DeactivateSubscriptionResponseDto deactivateSubscription(
            @Parameter(description = "ID of the subscription", example = "5")
            @PathVariable Integer id
    ) {
        return DeactivateSubscriptionResponseDto.fromEntity(service.deactivate(id));
    }
}
