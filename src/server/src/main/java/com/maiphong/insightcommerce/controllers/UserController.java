package com.maiphong.insightcommerce.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
@Tag(name = "Users", description = "Users Management APIs")
public class UserController {

    private final IUserService userService;
    private final PagedResourcesAssembler<UserMasterDTO> pagedResourcesAssembler;

    public UserController(IUserService userService, PagedResourcesAssembler<UserMasterDTO> pagedResourcesAssembler) {
        this.userService = userService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping
    @Operation(summary = "Get all users", description = "Get all users API")
    @ApiResponse(responseCode = "200", description = "Return all users")
    public ResponseEntity<List<UserBaseDTO>> getAll() {
        var users = userService.getAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/searchByName")
    @Operation(summary = "Get user by name", description = "Get users by name API")
    @ApiResponse(responseCode = "200", description = "Return users match name request")
    public ResponseEntity<List<UserMasterDTO>> searchByName(@RequestParam(required = false) String keyword) {
        var users = userService.search(keyword);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/search")
    @Operation(summary = "Search user paginated", description = "Search user paginated API")
    @ApiResponse(responseCode = "200", description = "Return users match keyword with pagination")
    public ResponseEntity<CustomPageResponse<EntityModel<UserMasterDTO>>> searchPaginated(
            @RequestBody UserSearchDTO userSearchDTO) {

        Page<UserMasterDTO> users = userService.searchPaginated(userSearchDTO);

        var pagedModel = pagedResourcesAssembler.toModel(users);

        var response = new CustomPageResponse<EntityModel<UserMasterDTO>>(pagedModel.getContent(),
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
    public ResponseEntity<Object> create(@Valid @RequestBody UserCreateUpdateDTO userDTO,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        var newUser = userService.create(userDTO);

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
            @Valid @RequestBody UserCreateUpdateDTO userDTO,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        var updatedUser = userService.update(id, userDTO);

        if (updatedUser == null) {
            return ResponseEntity.badRequest().body("Failed to update user");
        }

        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user by id")
    @ApiResponse(responseCode = "200", description = "Return true if delete successfully")
    @ApiResponse(responseCode = "400", description = "Return error message if delete failed")
    public ResponseEntity<Object> delete(@PathVariable UUID id) {

        var result = userService.delete(id);

        if (!result) {
            return ResponseEntity.badRequest().body("Failed to delete user");
        }

        return ResponseEntity.ok(result);
    }

    @PutMapping("/update-profile")
    @Operation(summary = "Update profile users")
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
    @Operation(summary = "Activate or Deactivate user")
    @ApiResponse(responseCode = "200", description = "Return true if operation is successfully")
    @ApiResponse(responseCode = "400", description = "Return error message if operation failed")
    public ResponseEntity<Object> toggleActiveStatus(@PathVariable UUID id,
            @Valid @RequestBody ChangeStatusDTO request,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        var result = userService.toggleActiveStatus(id, request);

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
        var result = userService.changeAvatar(request);

        if (!result) {
            return ResponseEntity.badRequest().body("Failed to change password");
        }

        return ResponseEntity.ok(result);
    }

}
