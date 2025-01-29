package com.maiphong.insightcommerce.dtos.security.role;

import com.maiphong.insightcommerce.dtos.BaseDTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO extends BaseDTO {
    private String name;

    private String description;
}
