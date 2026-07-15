package com.streaming.admin.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountSubscriptionRequestDto {

    @NotNull(message = "Account is required")
    private Integer idAccount;

    @NotNull(message = "App user is required")
    private Integer idAppUser;

    @NotNull(message = "Amount paid is required")
    private BigDecimal amountPaid;

    @NotNull(message = "Expiration date is required")
    private LocalDateTime expirationDate;

    @NotBlank(message = "Payment status is required")
    @Size(max = 20)
    private String paymentStatus;

    private LocalDateTime startDate;

    private LocalDateTime paymentDate;

    @Size(max = 990)
    private String description;
}
