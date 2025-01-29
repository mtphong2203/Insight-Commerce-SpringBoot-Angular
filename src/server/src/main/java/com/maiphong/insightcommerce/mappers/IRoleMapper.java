package com.maiphong.insightcommerce.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import com.maiphong.insightcommerce.dtos.security.role.RoleBaseDTO;
import com.maiphong.insightcommerce.dtos.security.role.RoleCreateUpdateDTO;
import com.maiphong.insightcommerce.dtos.security.role.RoleMasterDTO;
import com.maiphong.insightcommerce.entities.security.Role;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IRoleMapper {

    RoleMasterDTO toMasterDTO(Role entity);

    RoleBaseDTO toBaseDTO(Role entity);

    Role toEntity(RoleCreateUpdateDTO dto);

    // Keep the insertedAt, updatedAt, deletedAt fields as they are
    @Mapping(target = "insertedAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    void updateEntity(RoleCreateUpdateDTO dto, @MappingTarget Role entity);
}
