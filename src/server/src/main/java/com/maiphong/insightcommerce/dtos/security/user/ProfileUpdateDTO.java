package com.maiphong.insightcommerce.dtos.security.user;

import java.util.Date;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class ProfileUpdateDTO {
    @NotBlank(message = "Full Name is required")
    @Length(min = 2, max = 255, message = "First Name must be between 2 and 255 characters")
    private String fullName;

    @NotBlank(message = "Email is required")
    @Length(min = 2, max = 50, message = "Email must be between 2 and 50 characters")
    private String email;

    @NotBlank(message = "Phone Number is required")
    @Length(min = 10, max = 20, message = "Phone Number must be between 10 and 20 characters")
    private String phoneNumber;

    @NotNull(message = "Date of Birth is required")
    @Past(message = "Date of Birth must be in the past")
    private Date dateOfBirth;

    @NotBlank(message = "Address is required")
    @Length(min = 2, max = 255, message = "Address must be between 2 and 255 characters")
    private String address;

    @NotNull(message = "Gender is required")
    private boolean gender;

}
