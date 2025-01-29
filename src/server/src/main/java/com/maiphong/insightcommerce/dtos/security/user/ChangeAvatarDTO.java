package com.maiphong.insightcommerce.dtos.security.user;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class ChangeAvatarDTO {
    @NotBlank(message = "Avatar URL is required")
    @Length(max = 500, message = "Avatar URL must be less than 500 characters")
    private String avatarUrl;

}
