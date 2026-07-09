package com.streaming.admin.dto.response;

import com.streaming.admin.entity.AccountSubscription;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeactivateSubscriptionResponseDto {

    private Integer idAccountDetail;
    private Boolean isActive;
    private String message;

    public static DeactivateSubscriptionResponseDto fromEntity(AccountSubscription entity) {
        return DeactivateSubscriptionResponseDto.builder()
                .idAccountDetail(entity.getIdAccountDetail())
                .isActive(entity.getIsActive())
                .message("Subscription deactivated successfully")
                .build();
    }
}
