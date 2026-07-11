package com.streaming.admin.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppUserRequestDto {

    @NotBlank(message = "Name is required")
    @Size(max = 45)
    private String name;

    @NotBlank(message = "Last name is required")
    @Size(max = 45)
    private String lastName;

    @Size(max = 45)
    private String nickName;

    @Size(max = 20)
    private String ci;

    private LocalDate dateOfBirth;

    @NotBlank(message = "Phone number is required")
    @Size(max = 20)
    private String phoneNumber;
}
