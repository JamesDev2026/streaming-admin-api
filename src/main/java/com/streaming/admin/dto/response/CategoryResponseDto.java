package com.streaming.admin.dto.response;

import com.streaming.admin.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryResponseDto {

    private Integer idCategory;
    private String name;
    private String description;

    public static CategoryResponseDto fromEntity(Category entity) {
        return CategoryResponseDto.builder()
                .idCategory(entity.getIdCategory())
                .name(entity.getName())
                .description(entity.getDescription())
                .build();
    }
}
