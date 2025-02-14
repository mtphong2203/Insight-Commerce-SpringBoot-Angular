package com.maiphong.insightcommerce.services.security;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;

import com.maiphong.insightcommerce.dtos.security.role.RoleBaseDTO;
import com.maiphong.insightcommerce.dtos.security.role.RoleCreateUpdateDTO;
import com.maiphong.insightcommerce.dtos.security.role.RoleMasterDTO;
import com.maiphong.insightcommerce.dtos.security.role.RoleSearchDTO;

public interface IRoleService {
    List<RoleBaseDTO> findAll();

    RoleBaseDTO findByName(String name);

    List<RoleMasterDTO> search(String keyword);

    Page<RoleMasterDTO> search(RoleSearchDTO request);

    RoleMasterDTO findById(String id);

    RoleMasterDTO create(RoleCreateUpdateDTO roleDTO);

    RoleMasterDTO update(UUID id, RoleCreateUpdateDTO roleDTO);

    boolean delete(UUID id, boolean hardDelete);
}
