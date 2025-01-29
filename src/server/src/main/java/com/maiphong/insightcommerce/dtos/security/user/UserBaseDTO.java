package com.maiphong.insightcommerce.dtos.security.user;

import com.maiphong.insightcommerce.dtos.BaseDTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class UserBaseDTO extends BaseDTO {
    private String fullName;

    private String username;

    private String email;

    private String phoneNumber;
}
