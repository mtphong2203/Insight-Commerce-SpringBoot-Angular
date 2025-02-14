package com.maiphong.insightcommerce.dtos.security.auth;

import com.maiphong.insightcommerce.dtos.security.user.UserInformationDTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {
    private String accessToken;
    private UserInformationDTO user;
}
