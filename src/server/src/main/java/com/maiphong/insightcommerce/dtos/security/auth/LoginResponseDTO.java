package com.maiphong.insightcommerce.dtos.security.auth;

import com.maiphong.insightcommerce.dtos.security.user.UserInformationDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {
    private String accessToken;
    private UserInformationDTO user;
}
