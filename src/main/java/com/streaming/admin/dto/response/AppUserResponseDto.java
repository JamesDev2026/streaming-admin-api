package com.streaming.admin.dto.response;

import com.streaming.admin.entity.AppUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppUserResponseDto {

    private Integer idAppUser;
    private String name;
    private String lastName;
    private String nickName;
    private String ci;
    private LocalDate dateOfBirth;
    private String phoneNumber;

    public static AppUserResponseDto fromEntity(AppUser entity) {
        return AppUserResponseDto.builder()
                .idAppUser(entity.getIdAppUser())
                .name(entity.getName())
                .lastName(entity.getLastName())
                .nickName(entity.getNickName())
                .ci(entity.getCi())
                .dateOfBirth(entity.getDateOfBirth())
                .phoneNumber(entity.getPhoneNumber())
                .build();
    }
}
