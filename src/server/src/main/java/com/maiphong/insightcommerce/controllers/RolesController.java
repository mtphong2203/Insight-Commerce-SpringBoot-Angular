package com.maiphong.insightcommerce.controllers;

import java.util.*;

import org.springframework.data.domain.*;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.maiphong.insightcommerce.dtos.security.role.RoleBaseDTO;
import com.maiphong.insightcommerce.dtos.security.role.RoleCreateUpdateDTO;
import com.maiphong.insightcommerce.dtos.security.role.RoleMasterDTO;
import com.maiphong.insightcommerce.dtos.security.role.RoleSearchDTO;
import com.maiphong.insightcommerce.mappers.CustomPageResponse;
import com.maiphong.insightcommerce.services.security.IRoleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/roles")
@Tag(name = "Roles", description = "APIs for managing roles")
public class RolesController {
    private final IRoleService roleService;
    private final PagedResourcesAssembler<RoleMasterDTO> pagedResourcesAssembler;

    public RolesController(IRoleService roleService, PagedResourcesAssembler<RoleMasterDTO> pagedResourcesAssembler) {
        this.roleService = roleService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping
    @Operation(summary = "Get all roles")
    @ApiResponse(responseCode = "200", description = "Return all roles")
    public ResponseEntity<List<RoleBaseDTO>> getAll() {
        var roles = roleService.findAll();
        return ResponseEntity.ok(roles);
    }

    @GetMapping("/searchByName")
    @Operation(summary = "Search roles by role number")
    @ApiResponse(responseCode = "200", description = "Return roles that match the role number")
    public ResponseEntity<List<RoleMasterDTO>> searchByName(
            @RequestParam(required = false) String keyword) {
        var roles = roleService.search(keyword);
        return ResponseEntity.ok(roles);
    }

    @GetMapping("/search")
    @Operation(summary = "Search roles with pagination")
    @ApiResponse(responseCode = "200", description = "Return roles that match the keyword with pagination")
    public ResponseEntity<Object> searchPaginated(@ModelAttribute RoleSearchDTO request) {
        Page<RoleMasterDTO> roles = roleService.search(request);

        var pagedModel = pagedResourcesAssembler.toModel(roles);

        var response = new CustomPageResponse<EntityModel<RoleMasterDTO>>(
                pagedModel.getContent(), pagedModel.getLinks(), pagedModel.getMetadata());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get role by id")
    @ApiResponse(responseCode = "200", description = "Return role that match the id")
    public ResponseEntity<RoleMasterDTO> getById(@PathVariable String id) {
        var role = roleService.findById(id);
        return ResponseEntity.ok(role);
    }

    @PostMapping()
    @Operation(summary = "Create new role")
    @ApiResponse(responseCode = "200", description = "Return created role")
    @ApiResponse(responseCode = "400", description = "Return error message if create failed")
    public ResponseEntity<Object> create(@Valid @RequestBody RoleCreateUpdateDTO request,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        var newRole = roleService.create(request);

        if (newRole == null) {
            return ResponseEntity.badRequest().body("Failed to create role");
        }

        return ResponseEntity.ok(newRole);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update role by id")
    @ApiResponse(responseCode = "200", description = "Return updated role")
    @ApiResponse(responseCode = "400", description = "Return error message if update failed")
    public ResponseEntity<Object> update(
            @PathVariable UUID id,
            @Valid @RequestBody RoleCreateUpdateDTO request,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        var updatedRole = roleService.update(id, request);

        if (updatedRole == null) {
            return ResponseEntity.badRequest().body("Failed to update role");
        }

        return ResponseEntity.ok(updatedRole);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete role by id")
    @ApiResponse(responseCode = "200", description = "Return true if delete successfully")
    @ApiResponse(responseCode = "400", description = "Return error message if delete failed")
    public ResponseEntity<Object> delete(@PathVariable UUID id,
            @RequestParam(required = false, defaultValue = "false") boolean hardDelete) {

        var result = roleService.delete(id, hardDelete);

        if (!result) {
            return ResponseEntity.badRequest().body("Failed to delete role");
        }

        return ResponseEntity.ok(result);
    }
}
