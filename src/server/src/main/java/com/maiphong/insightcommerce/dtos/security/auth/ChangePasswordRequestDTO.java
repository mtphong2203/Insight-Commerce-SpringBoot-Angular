package com.maiphong.insightcommerce.dtos.security.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordRequestDTO {
    private String token;
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
}
