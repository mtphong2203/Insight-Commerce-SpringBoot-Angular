package com.maiphong.insightcommerce.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maiphong.insightcommerce.dtos.security.auth.ChangePasswordRequestDTO;
import com.maiphong.insightcommerce.dtos.security.auth.LoginRequestDTO;
import com.maiphong.insightcommerce.dtos.security.auth.LoginResponseDTO;
import com.maiphong.insightcommerce.dtos.security.auth.ResetPasswordRequestDTO;
import com.maiphong.insightcommerce.dtos.security.auth.ValidateResetPasswordTokenRequestDTO;
import com.maiphong.insightcommerce.services.security.IAuthService;
import com.maiphong.insightcommerce.services.security.ITokenService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "Authentication")
public class AuthController {
    private final IAuthService authService;
    private final AuthenticationManagerBuilder managerBuilder;
    private final ITokenService tokenService;

    public AuthController(IAuthService authService, AuthenticationManagerBuilder managerBuilder,
            ITokenService tokenService) {
        this.authService = authService;
        this.managerBuilder = managerBuilder;
        this.tokenService = tokenService;
    }

    // Login
    @PostMapping("/login")
    @Operation(summary = "Login with username and password", description = "Login API")
    public ResponseEntity<Object> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        // Create authentication token with username and password
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginRequestDTO.getUsername(), loginRequestDTO.getPassword());

        // Authenticate user
        Authentication authentication = managerBuilder.getObject().authenticate(authenticationToken);

        // Set authentication to security context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        var userInformationDTO = authService.getUserInformationDTO(loginRequestDTO.getUsername());

        // Generate JWT Token
        String accessToken = tokenService.generateToken(authentication, userInformationDTO);

        LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
        loginResponseDTO.setAccessToken(accessToken);
        loginResponseDTO.setUser(userInformationDTO);

        return ResponseEntity.ok(loginResponseDTO);

    }

    // Request reset password API
    @PostMapping("/request-reset-password")
    @Operation(summary = "Reset password", description = "Reset password API")
    public ResponseEntity<Object> resetPassword(@Valid @RequestBody ResetPasswordRequestDTO requestDTO,
            BindingResult bindingResult) {

        // Validate request
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        var result = authService.requestResetPassword(requestDTO);

        return ResponseEntity.ok(result);
    }

    // Validate reset password token API
    @PostMapping("/validate-reset-password-token")
    @Operation(summary = "Validate reset password token", description = "Validate reset password token API")
    public ResponseEntity<Object> validateResetPasswordToken(
            @Valid @RequestBody ValidateResetPasswordTokenRequestDTO validateDTO,
            BindingResult bindingResult) {

        // Validate request
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        var result = authService.validateResetPasswordToken(validateDTO);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/change-password")
    @Operation(summary = "Change password", description = "Change password API")
    public ResponseEntity<Object> changePassword(@Valid @RequestBody ChangePasswordRequestDTO changePasswordRequestDTO,
            BindingResult bindingResult) {

        // Validate request
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        var result = authService.changePassword(changePasswordRequestDTO);

        return ResponseEntity.ok(result);
    }

}
