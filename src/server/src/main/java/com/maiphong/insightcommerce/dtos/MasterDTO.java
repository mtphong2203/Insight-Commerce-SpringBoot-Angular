package com.maiphong.insightcommerce.dtos;

import java.time.ZonedDateTime;

import com.maiphong.insightcommerce.dtos.security.user.UserBaseDTO;

import lombok.*;
import lombok.Setter;

@Getter
@Setter
public class MasterDTO extends BaseDTO {
    private ZonedDateTime insertedAt;

    private UserBaseDTO insertedBy;

    private ZonedDateTime updatedAt;

    private UserBaseDTO updatedBy;

    private ZonedDateTime deletedAt;

    private UserBaseDTO deletedBy;
}