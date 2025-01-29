package com.maiphong.insightcommerce.dtos.security.user;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class ChangePasswordDTO {

    @NotBlank(message = "Current password is required")
    @Length(min = 8, max = 20, message = "Current password must be between 8 and 20 characters")
    private String oldPassword;

    @NotBlank(message = "New password is required")
    @Length(min = 8, max = 20, message = "New password must be between 8 and 20 characters")

    // password must contain at least one uppercase letter, one lowercase letter,
    // one number and one special character: 7ou<$Y?-, Admin@1234
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&-])[A-Za-z\\d@$!%*?&-]{8,20}$", message = "New password must contain at least one uppercase letter, one lowercase letter, one number and one special character")
    private String newPassword;

    @NotBlank(message = "Confirm password is required")
    @Length(min = 8, max = 20, message = "Confirm password must be between 8 and 20 characters")
    // password must contain at least one uppercase letter, one lowercase letter,
    // one number and one special character: 7ou<$Y?-, Admin@1234
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&-])[A-Za-z\\d@$!%*?&-]{8,20}$", message = "Confirm password must contain at least one uppercase letter, one lowercase letter, one number and one special character")
    private String confirmPassword;

}
