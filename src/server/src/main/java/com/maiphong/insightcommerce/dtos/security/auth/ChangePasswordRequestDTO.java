package com.maiphong.insightcommerce.dtos.security.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordRequestDTO {
    @NotBlank(message = "Token is required")
    private String token;

    @NotBlank(message = "Old password is required")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()_+|~=`{}\\[\\]:\";'<>?,./])[A-Za-z\\d!@#$%^&*()_+|~=`{}\\[\\]:\";'<>?,./]{8,20}$", message = "Password must be 8-20 characters long, contain at least one uppercase letter, one lowercase letter, one digit and one special character")
    private String oldPassword;

    @NotBlank(message = "New password is required")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()_+|~=`{}\\[\\]:\";'<>?,./])[A-Za-z\\d!@#$%^&*()_+|~=`{}\\[\\]:\";'<>?,./]{8,20}$", message = "Password must be 8-20 characters long, contain at least one uppercase letter, one lowercase letter, one digit and one special character")
    private String newPassword;

    @NotBlank(message = "Confirm password is required")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()_+|~=`{}\\[\\]:\";'<>?,./])[A-Za-z\\d!@#$%^&*()_+|~=`{}\\[\\]:\";'<>?,./]{8,20}$", message = "Password must be 8-20 characters long, contain at least one uppercase letter, one lowercase letter, one digit and one special character")
    private String confirmPassword;
}
