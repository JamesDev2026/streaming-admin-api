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
public class AccountRequestDto {

    @NotBlank(message = "Name is required")
    @Size(max = 45)
    private String name;

    @NotBlank(message = "User name is required")
    @Size(max = 45)
    private String userName;

    @NotBlank(message = "Password is required")
    @Size(max = 255)
    private String password;

    @Size(max = 2000)
    private String description;

    private LocalDateTime purchaseDate;

    private LocalDateTime expirationDate;

    @NotNull(message = "Price is required")
    private BigDecimal price;

    @NotNull(message = "Max users is required")
    private Integer maxUsers;

    @NotNull(message = "Account type is required")
    private Integer idAccountType;

    @NotNull(message = "Category is required")
    private Integer idCategory;

    @NotNull(message = "Panel is required")
    private Integer idPanel;
}
