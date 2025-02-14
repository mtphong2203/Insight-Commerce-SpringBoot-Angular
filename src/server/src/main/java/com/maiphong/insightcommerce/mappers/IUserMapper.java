package com.maiphong.insightcommerce.mappers;

import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.*;

import com.maiphong.insightcommerce.dtos.security.auth.RegisterRequestDTO;
import com.maiphong.insightcommerce.dtos.security.user.UserBaseDTO;
import com.maiphong.insightcommerce.dtos.security.user.UserCreateUpdateDTO;
import com.maiphong.insightcommerce.dtos.security.user.UserInformationDTO;
import com.maiphong.insightcommerce.dtos.security.user.UserMasterDTO;
import com.maiphong.insightcommerce.entities.security.Role;
import com.maiphong.insightcommerce.entities.security.User;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IUserMapper {

    UserMasterDTO toMasterDTO(User entity);

    UserBaseDTO toBaseDTO(User entity);

    User toEntity(UserCreateUpdateDTO dto);

    User toEntity(RegisterRequestDTO dto);

    @Mapping(target = "roles", source = "roles", qualifiedByName = "mapRoles")
    UserInformationDTO toInformationDTO(User entity);

    // Keep the insertedAt, updatedAt, deletedAt fields as they are
    @Mapping(target = "insertedAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    void updateEntity(UserCreateUpdateDTO dto, @MappingTarget User entity);

    @Named("mapRoles")
    default Set<String> mapRoles(Set<Role> roles) {
        return roles.stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
    }

}
