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
public class AccountTypeRequestDto {

    @NotBlank(message = "Name is required")
    @Size(max = 45)
    private String name;

    @Size(max = 990)
    private String description;
}
