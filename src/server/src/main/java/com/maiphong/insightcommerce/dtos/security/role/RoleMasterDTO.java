package com.maiphong.insightcommerce.dtos.security.role;

import com.maiphong.insightcommerce.dtos.MasterDTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleMasterDTO extends MasterDTO {
    private String name;

    private String description;
}
