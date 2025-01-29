package com.maiphong.insightcommerce.dtos.security.user;

import java.util.Set;
import java.util.UUID;

import com.maiphong.insightcommerce.dtos.SearchDTO;

import lombok.*;

@Getter
@Setter
public class UserSearchDTO extends SearchDTO {
    private Set<UUID> roleIds;

    private Boolean gender;

    private Boolean active;
}
