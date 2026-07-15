package com.streaming.admin.dto.response;

import com.streaming.admin.entity.Account;
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
public class AccountResponseDto {

    private Integer idAccount;
    private String name;
    private String userName;
    private String password;
    private String description;
    private LocalDateTime purchaseDate;
    private LocalDateTime expirationDate;
    private BigDecimal price;
    private Integer maxUsers;
    private Integer idAccountType;
    private Integer idCategory;
    private Integer idPanel;

    public static AccountResponseDto fromEntity(Account entity) {
        return AccountResponseDto.builder()
                .idAccount(entity.getIdAccount())
                .name(entity.getName())
                .userName(entity.getUserName())
                .password(entity.getPassword())
                .description(entity.getDescription())
                .purchaseDate(entity.getPurchaseDate())
                .expirationDate(entity.getExpirationDate())
                .price(entity.getPrice())
                .maxUsers(entity.getMaxUsers())
                .idAccountType(entity.getIdAccountType())
                .idCategory(entity.getIdCategory())
                .idPanel(entity.getIdPanel())
                .build();
    }
}
