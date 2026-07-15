package com.streaming.admin.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdatePaymentStatusRequestDto {

    @NotBlank(message = "Payment status is required")
    @Size(max = 20)
    private String paymentStatus;
}
