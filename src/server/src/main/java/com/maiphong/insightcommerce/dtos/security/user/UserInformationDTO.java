package com.maiphong.insightcommerce.dtos.security.user;

import java.util.*;

import com.maiphong.insightcommerce.dtos.BaseDTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserInformationDTO extends BaseDTO {
    private String fullName;

    private String username;

    private String email;

    private String phoneNumber;

    private Date dateOfBirth;

    private boolean isActive;

    private boolean gender;

    private String avatar;

    private String address;

    private String note;

    private Set<String> roles;
}
