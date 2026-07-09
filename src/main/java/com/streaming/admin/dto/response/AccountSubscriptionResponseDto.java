package com.streaming.admin.dto.response;

import com.streaming.admin.entity.AccountSubscription;
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
public class AccountSubscriptionResponseDto {

    private Integer idAccountDetail;
    private Integer idAccount;
    private Integer idAppUser;
    private BigDecimal amountPaid;
    private LocalDateTime expirationDate;
    private String paymentStatus;
    private LocalDateTime startDate;
    private LocalDateTime paymentDate;
    private Boolean isActive;
    private String description;

    public static AccountSubscriptionResponseDto fromEntity(AccountSubscription entity) {
        return AccountSubscriptionResponseDto.builder()
                .idAccountDetail(entity.getIdAccountDetail())
                .idAccount(entity.getIdAccount())
                .idAppUser(entity.getIdAppUser())
                .amountPaid(entity.getAmountPaid())
                .expirationDate(entity.getExpirationDate())
                .paymentStatus(entity.getPaymentStatus())
                .startDate(entity.getStartDate())
                .paymentDate(entity.getPaymentDate())
                .isActive(entity.getIsActive())
                .description(entity.getDescription())
                .build();
    }
}
