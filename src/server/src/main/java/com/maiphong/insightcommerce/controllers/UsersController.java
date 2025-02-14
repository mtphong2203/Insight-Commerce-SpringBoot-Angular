package com.maiphong.insightcommerce.controllers;

import java.util.*;

import org.springframework.data.domain.*;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.maiphong.insightcommerce.dtos.security.user.ChangeAvatarDTO;
import com.maiphong.insightcommerce.dtos.security.user.ChangePasswordDTO;
import com.maiphong.insightcommerce.dtos.security.user.ChangeStatusDTO;
import com.maiphong.insightcommerce.dtos.security.user.ProfileUpdateDTO;
import com.maiphong.insightcommerce.dtos.security.user.UserBaseDTO;
import com.maiphong.insightcommerce.dtos.security.user.UserCreateUpdateDTO;
import com.maiphong.insightcommerce.dtos.security.user.UserMasterDTO;
import com.maiphong.insightcommerce.dtos.security.user.UserSearchDTO;
import com.maiphong.insightcommerce.mappers.CustomPageResponse;
import com.maiphong.insightcommerce.services.security.IUserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Users", description = "APIs for managing users")
public class UsersController {
    private final IUserService userService;
    private final PagedResourcesAssembler<UserMasterDTO> pagedResourcesAssembler;

    public UsersController(IUserService userService, PagedResourcesAssembler<UserMasterDTO> pagedResourcesAssembler) {
        this.userService = userService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping
    @Operation(summary = "Get all users")
    @ApiResponse(responseCode = "200", description = "Return all users")
    public ResponseEntity<List<UserBaseDTO>> getAll() {
        var users = userService.getAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/searchByKeyword")
    @Operation(summary = "Search users by user number")
    @ApiResponse(responseCode = "200", description = "Return users that match the user number")
    public ResponseEntity<List<UserMasterDTO>> searchByKeyword(
            @RequestParam(required = false) String keyword) {
        var users = userService.search(keyword);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/search")
    @Operation(summary = "Search users with pagination")
    @ApiResponse(responseCode = "200", description = "Return users that match the keyword with pagination")
    public ResponseEntity<CustomPageResponse<EntityModel<UserMasterDTO>>> search(
            @ModelAttribute UserSearchDTO searchDTO) {
        // Fetch jobs based on criteria
        Page<UserMasterDTO> jobs = userService.search(searchDTO);

        // Convert to paged model
        var pagedModel = pagedResourcesAssembler.toModel(jobs);

        // Prepare response
        var response = new CustomPageResponse<EntityModel<UserMasterDTO>>(
                pagedModel.getContent(),
                pagedModel.getLinks(),
                pagedModel.getMetadata());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by id")
    @ApiResponse(responseCode = "200", description = "Return user that match the id")
    public ResponseEntity<UserMasterDTO> getById(@PathVariable String id) {
        var user = userService.getById(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping()
    @Operation(summary = "Create new user")
    @ApiResponse(responseCode = "200", description = "Return created user")
    @ApiResponse(responseCode = "400", description = "Return error message if create failed")
    public ResponseEntity<Object> create(@Valid @RequestBody UserCreateUpdateDTO request,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        var newUser = userService.create(request);

        if (newUser == null) {
            return ResponseEntity.badRequest().body("Failed to create user");
        }

        return ResponseEntity.ok(newUser);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user by id")
    @ApiResponse(responseCode = "200", description = "Return updated user")
    @ApiResponse(responseCode = "400", description = "Return error message if update failed")
    public ResponseEntity<Object> update(
            @PathVariable UUID id,
            @Valid @RequestBody UserCreateUpdateDTO request,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        var updatedUser = userService.update(id, request);

        if (updatedUser == null) {
            return ResponseEntity.badRequest().body("Failed to update user");
        }

        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user by id")
    @ApiResponse(responseCode = "200", description = "Return true if delete successfully")
    @ApiResponse(responseCode = "400", description = "Return error message if delete failed")
    public ResponseEntity<Object> delete(@PathVariable UUID id,
            @RequestParam(required = false, defaultValue = "false") boolean hardDelete) {

        var result = userService.delete(id, hardDelete);

        if (!result) {
            return ResponseEntity.badRequest().body("Failed to delete user");
        }

        return ResponseEntity.ok(result);
    }

    @PutMapping("/update-profile")
    @Operation(summary = "Update user profile")
    @ApiResponse(responseCode = "200", description = "Return updated user")
    @ApiResponse(responseCode = "400", description = "Return error message if update failed")
    public ResponseEntity<Object> updateProfile(@Valid @RequestBody ProfileUpdateDTO request,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        var result = userService.updateProfile(request);

        if (result == null) {
            return ResponseEntity.badRequest().body("Failed to update user");
        }

        return ResponseEntity.ok(result);
    }

    @PutMapping("/change-password")
    @Operation(summary = "Change user password")
    @ApiResponse(responseCode = "200", description = "Return true if change password successfully")
    @ApiResponse(responseCode = "400", description = "Return error message if change password failed")
    public ResponseEntity<Object> changePassword(@Valid @RequestBody ChangePasswordDTO request,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        var result = userService.changePassword(request);

        if (!result) {
            return ResponseEntity.badRequest().body("Failed to change password");
        }

        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}/change-status")
    @Operation(summary = "Activate or Deactivate a user")
    @ApiResponse(responseCode = "200", description = "Return true if operation is successful")
    @ApiResponse(responseCode = "400", description = "Return error message if operation failed")
    public ResponseEntity<Object> toggleActiveStatus(@PathVariable UUID id,
            @Valid @RequestBody ChangeStatusDTO request, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        boolean result = userService.toggleActiveStatus(id, request);

        if (!result) {
            return ResponseEntity.badRequest().body("Failed to change status");
        }

        return ResponseEntity.ok(result);
    }

    @PutMapping("/change-avatar")
    @Operation(summary = "Update user avatar URL")
    @ApiResponse(responseCode = "200", description = "Return updated avatar URL")
    @ApiResponse(responseCode = "400", description = "Return error message if update failed")
    public ResponseEntity<Object> updateAvatar(@Valid @RequestBody ChangeAvatarDTO request,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        boolean result = userService.changeAvatar(request);
        if (!result) {
            return ResponseEntity.badRequest().body("Failed to update avatar");
        }
        return ResponseEntity.ok(result);
    }
}
