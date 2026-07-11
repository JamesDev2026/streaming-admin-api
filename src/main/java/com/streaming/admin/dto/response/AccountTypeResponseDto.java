package com.streaming.admin.dto.response;

import com.streaming.admin.entity.AccountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountTypeResponseDto {

    private Integer idAccountType;
    private String name;
    private String description;

    public static AccountTypeResponseDto fromEntity(AccountType entity) {
        return AccountTypeResponseDto.builder()
                .idAccountType(entity.getIdAccountType())
                .name(entity.getName())
                .description(entity.getDescription())
                .build();
    }
}
