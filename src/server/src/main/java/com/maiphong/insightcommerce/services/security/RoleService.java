package com.maiphong.insightcommerce.services.security;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.maiphong.insightcommerce.core.constants.CommonConstant;
import com.maiphong.insightcommerce.dtos.security.role.RoleBaseDTO;
import com.maiphong.insightcommerce.dtos.security.role.RoleCreateUpdateDTO;
import com.maiphong.insightcommerce.dtos.security.role.RoleMasterDTO;
import com.maiphong.insightcommerce.dtos.security.role.RoleSearchDTO;
import com.maiphong.insightcommerce.entities.security.Role;
import com.maiphong.insightcommerce.exceptions.ResourceNotFoundException;
import com.maiphong.insightcommerce.mappers.IRoleMapper;
import com.maiphong.insightcommerce.repositories.security.IRoleRepository;

@Service
@Transactional
public class RoleService implements IRoleService {

    private final IRoleRepository roleRepository;
    private final IRoleMapper roleMapper;

    public RoleService(IRoleRepository roleRepository, IRoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
    }

    @Override
    public List<RoleBaseDTO> findAll() {
        List<Role> roles = roleRepository.findAll();

        return roles.stream().map(roleMapper::toBaseDTO).toList();
    }

    @Override
    public List<RoleMasterDTO> search(String keyword) {

        Specification<Role> spec = buildKeywordSpecification(keyword);
        List<Role> roles = roleRepository.findAll(spec);

        return roles.stream().map(roleMapper::toMasterDTO).toList();
    }

    @Override
    public Page<RoleMasterDTO> search(RoleSearchDTO request) {
        Specification<Role> spec = buildKeywordSpecification(request.getKeyword());

        if (spec == null) {
            spec = Specification.where(null);
        }

        Page<Role> roles = roleRepository.findAll(spec, request.toPageable());

        return roles.map(roleMapper::toMasterDTO);
    }

    /**
     * Helper method
     * 
     * @param keyword
     * @return
     */
    private Specification<Role> buildKeywordSpecification(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return null;
        }

        return (root, query, cb) -> cb.or(
                cb.like(cb.lower(root.get("name")), "%" + keyword.toLowerCase() + "%"),
                cb.like(cb.lower(root.get("description")), "%" + keyword.toLowerCase() + "%"));
    }

    @Override
    public RoleBaseDTO findByName(String name) {
        var role = roleRepository.findByName(name).orElse(null);

        if (role == null) {
            return null;
        }

        return roleMapper.toBaseDTO(role);
    }

    @Override
    public RoleMasterDTO findById(String id) {
        Role role = roleRepository.findById(UUID.fromString(id)).orElse(null);

        if (role == null) {
            throw new ResourceNotFoundException(CommonConstant.ROLE_NOT_FOUND);
        }

        return roleMapper.toMasterDTO(role);
    }

    @Override
    public RoleMasterDTO create(RoleCreateUpdateDTO roleDTO) {
        if (roleDTO == null) {
            throw new ResourceNotFoundException(CommonConstant.ROLE_NOT_FOUND);
        }

        Role existRole = roleRepository.findByName(roleDTO.getName()).orElse(null);

        if (existRole != null) {
            throw new IllegalArgumentException("Role already exist!");
        }

        Role role = roleMapper.toEntity(roleDTO);

        role = roleRepository.save(role);

        return roleMapper.toMasterDTO(role);
    }

    @Override
    public RoleMasterDTO update(UUID id, RoleCreateUpdateDTO roleDTO) {
        if (roleDTO == null) {
            throw new ResourceNotFoundException(CommonConstant.ROLE_NOT_FOUND);
        }

        Role existRole = roleRepository.findByName(roleDTO.getName()).orElse(null);

        if (existRole != null && existRole.getId().equals(id)) {
            throw new IllegalArgumentException("Role already exist!");
        }

        Role role = roleRepository.findById(id).orElse(null);

        if (role == null) {
            throw new ResourceNotFoundException(CommonConstant.ROLE_NOT_FOUND);
        }

        roleMapper.updateEntity(roleDTO, role);
        role.setId(id);

        role = roleRepository.save(role);

        return roleMapper.toMasterDTO(role);
    }

    @Override
    public boolean delete(UUID id, boolean hardDelete) {
        var role = roleRepository.findById(id).orElse(null);

        if (role == null) {
            throw new ResourceNotFoundException(CommonConstant.USER_NOT_FOUND);
        }

        if (hardDelete) {
            roleRepository.delete(role);
        } else {
            role.setDeletedAt(ZonedDateTime.now());
            roleRepository.save(role);
        }
        return true;
    }

}
