package com.maiphong.insightcommerce.dtos.security.user;

import java.util.Date;

import com.maiphong.insightcommerce.dtos.BaseDTO;
import com.maiphong.insightcommerce.dtos.security.role.RoleDTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO extends BaseDTO {
    private String fullName;

    private String username;

    private String email;

    private String phoneNumber;

    private Date dateOfBirth;

    private boolean isActive;

    private boolean gender;

    private String address;

    private String note;

    private RoleDTO role;

}
