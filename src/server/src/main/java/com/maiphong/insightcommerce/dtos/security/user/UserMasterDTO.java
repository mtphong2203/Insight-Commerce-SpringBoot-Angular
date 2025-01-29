package com.maiphong.insightcommerce.dtos.security.user;

import java.util.Date;
import java.util.Set;

import com.maiphong.insightcommerce.dtos.MasterDTO;
import com.maiphong.insightcommerce.dtos.security.role.RoleDTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class UserMasterDTO extends MasterDTO {
    private String fullName;

    private String username;

    private String email;

    private String phoneNumber;

    private Date dateOfBirth;

    private boolean isActive;

    private boolean gender;

    private String address;

    private String note;

    private Set<RoleDTO> role;

}
