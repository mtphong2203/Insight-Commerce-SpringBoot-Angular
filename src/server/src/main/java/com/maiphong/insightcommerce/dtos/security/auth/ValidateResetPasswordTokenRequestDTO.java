package com.maiphong.insightcommerce.dtos.security.auth;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidateResetPasswordTokenRequestDTO {

    @NotBlank(message = "Token is required")
    @Length(min = 5, max = 255, message = "Token must be between 5 and 255 characters")
    private String token;
}
