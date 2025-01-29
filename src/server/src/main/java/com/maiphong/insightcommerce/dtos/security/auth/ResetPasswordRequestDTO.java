package com.maiphong.insightcommerce.dtos.security.auth;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordRequestDTO {
    @NotBlank(message = "Email is required")
    @Length(min = 5, max = 255, message = "Email must be between 5 and 255 characters")
    private String email;
}
