package com.maiphong.insightcommerce.dtos.security.auth;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDTO {
    @NotBlank(message = "Username is required")
    @Length(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    @NotBlank(message = "Password is required")
    @Length(min = 8, max = 50, message = "Password must be between 3 and 50 characters")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()_+|~=`{}\\[\\]:\";'<>?,./])[A-Za-z\\d!@#$%^&*()_+|~=`{}\\[\\]:\";'<>?,./]{8,20}$", message = "Password must be 8-20 characters long, contain at least one uppercase letter, one lowercase letter, one digit and one special character")
    private String password;
}
