package com.streaming.admin.dto.response;

import com.streaming.admin.entity.Panel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PanelResponseDto {

    private Integer idPanel;
    private String name;
    private String url;
    private String description;

    public static PanelResponseDto fromEntity(Panel entity) {
        return PanelResponseDto.builder()
                .idPanel(entity.getIdPanel())
                .name(entity.getName())
                .url(entity.getUrl())
                .description(entity.getDescription())
                .build();
    }
}
