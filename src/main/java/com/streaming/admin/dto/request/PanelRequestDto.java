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
public class PanelRequestDto {

    @NotBlank(message = "Name is required")
    @Size(max = 45)
    private String name;

    @Size(max = 2000)
    private String url;

    @Size(max = 990)
    private String description;
}
