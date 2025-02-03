package com.maiphong.insightcommerce.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.maiphong.insightcommerce.dtos.security.role.RoleBaseDTO;
import com.maiphong.insightcommerce.dtos.security.role.RoleCreateUpdateDTO;
import com.maiphong.insightcommerce.dtos.security.role.RoleMasterDTO;
import com.maiphong.insightcommerce.mappers.CustomPageResponse;
import com.maiphong.insightcommerce.services.security.IRoleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/roles")
@Tag(name = "Roles", description = "Role Management APIs")
public class RoleController {

    private final IRoleService roleService;
    private final PagedResourcesAssembler<RoleMasterDTO> pagedResourcesAssembler;

    public RoleController(IRoleService roleService, PagedResourcesAssembler<RoleMasterDTO> pagedResourcesAssembler) {
        this.roleService = roleService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping
    @Operation(summary = "Get all roles", description = "Get all roles API")
    @ApiResponse(responseCode = "200", description = "Return all roles")
    public ResponseEntity<List<RoleBaseDTO>> getAll() {
        var roles = roleService.findAll();

        return ResponseEntity.ok(roles);
    }

    @GetMapping("/searchByName")
    @Operation(summary = "Search role by name", description = "Search role by name API")
    @ApiResponse(responseCode = "200", description = "Return roles match role number")
    public ResponseEntity<List<RoleMasterDTO>> searchByName(@RequestParam(required = false) String keyword) {
        var roles = roleService.findByName(keyword);

        return ResponseEntity.ok(roles);
    }

    @GetMapping("/search")
    @Operation(summary = "Search role paginated", description = "Search role paginated API")
    @ApiResponse(responseCode = "200", description = "Return roles match keyword with pagination")
    public ResponseEntity<Object> searchPaginated(@RequestParam(required = false) String keyword,
            @RequestParam(required = false, defaultValue = "name") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String order,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size) {
        Pageable pageable = null;

        if (order.equalsIgnoreCase("asc")) {
            pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        } else {
            pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        }

        var roles = roleService.findPaginated(keyword, pageable);

        var pagedModel = pagedResourcesAssembler.toModel(roles);

        // Get data, page, and links from pagedModel
        var response = new CustomPageResponse<EntityModel<RoleMasterDTO>>(pagedModel.getContent(),
                pagedModel.getLinks(), pagedModel.getMetadata());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get role by id", description = "Get role by id API")
    @ApiResponse(responseCode = "200", description = "Return role match id")
    public ResponseEntity<RoleMasterDTO> getById(@PathVariable String id) {
        var role = roleService.findById(id);
        return ResponseEntity.ok(role);
    }

    @PostMapping
    @Operation(summary = "Create new role", description = "Create new role API")
    @ApiResponse(responseCode = "200", description = "Return created role")
    @ApiResponse(responseCode = "400", description = "Return error message if create failed")
    public ResponseEntity<Object> create(@Valid @RequestBody RoleCreateUpdateDTO roleCreateUpdateDTO,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        var newRole = roleService.create(roleCreateUpdateDTO);

        if (newRole == null) {
            return ResponseEntity.badRequest().body("Failed to create role");
        }

        return ResponseEntity.ok(newRole);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update role", description = "Update role API")
    @ApiResponse(responseCode = "200", description = "Return role updated")
    @ApiResponse(responseCode = "400", description = "Return error message if update failed")
    public ResponseEntity<Object> update(@PathVariable UUID id,
            @Valid @RequestBody RoleCreateUpdateDTO roleCreateUpdateDTO,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        var updatedRole = roleService.update(id, roleCreateUpdateDTO);

        if (updatedRole == null) {
            return ResponseEntity.badRequest().body("Failed to update role");
        }

        return ResponseEntity.ok(updatedRole);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete role by id")
    @ApiResponse(responseCode = "200", description = "Return true if delete successfully")
    @ApiResponse(responseCode = "400", description = "Return error message if delete failed")
    public ResponseEntity<Object> delete(@PathVariable UUID id) {

        var result = roleService.delete(id);

        if (!result) {
            return ResponseEntity.badRequest().body("Failed to delete role");
        }

        return ResponseEntity.ok(result);
    }

}
