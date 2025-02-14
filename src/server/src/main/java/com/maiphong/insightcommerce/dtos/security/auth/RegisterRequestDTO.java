package com.maiphong.insightcommerce.dtos.security.auth;

import jakarta.validation.constraints.*;

import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDTO {
    @NotBlank(message = "Full Name is required")
    private String fullName;

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Email is invalid")
    private String email;

    @NotBlank(message = "Phone Number is required")
    private String phoneNumber;

    @NotNull(message = "Date of Birth is required")
    private LocalDate dateOfBirth;

    @NotBlank(message = "Address is required")
    private String address;

    private String avatar;

    private boolean gender;

    @NotBlank(message = "Password is required")
    @Length(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
    private String password;

    @NotBlank
    @Length(min = 8, max = 20, message = "Confirm Password must be between 8 and 20 characters")
    private String confirmPassword;
}
