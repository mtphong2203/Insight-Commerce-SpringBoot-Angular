package com.maiphong.insightcommerce.services.security;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.maiphong.insightcommerce.dtos.security.role.RoleBaseDTO;
import com.maiphong.insightcommerce.dtos.security.role.RoleCreateUpdateDTO;
import com.maiphong.insightcommerce.dtos.security.role.RoleMasterDTO;

public interface IRoleService {
    List<RoleBaseDTO> findAll();

    List<RoleMasterDTO> findByName(String keyword);

    Page<RoleMasterDTO> findPaginated(String keyword, Pageable pageable);

    RoleMasterDTO findById(String id);

    RoleMasterDTO create(RoleCreateUpdateDTO roleDTO);

    RoleMasterDTO update(UUID id, RoleCreateUpdateDTO roleDTO);

    boolean delete(UUID id);
}
