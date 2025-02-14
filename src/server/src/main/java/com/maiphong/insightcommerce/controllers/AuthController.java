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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.maiphong.insightcommerce.dtos.security.auth.ChangePasswordRequestDTO;
import com.maiphong.insightcommerce.dtos.security.auth.LoginRequestDTO;
import com.maiphong.insightcommerce.dtos.security.auth.LoginResponseDTO;
import com.maiphong.insightcommerce.dtos.security.auth.RegisterRequestDTO;
import com.maiphong.insightcommerce.dtos.security.auth.ResetPasswordRequestDTO;
import com.maiphong.insightcommerce.dtos.security.auth.ValidateResetPasswordTokenRequestDTO;
import com.maiphong.insightcommerce.services.security.IAuthService;
import com.maiphong.insightcommerce.services.security.ITokenService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "Authentication APIs")
public class AuthController {
    private final IAuthService authService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final ITokenService tokenService;

    public AuthController(IAuthService authService, AuthenticationManagerBuilder authenticationManagerBuilder,
            ITokenService tokenService) {
        this.authService = authService;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.tokenService = tokenService;
    }

    // Login API
    @PostMapping("/login")
    @Operation(summary = "Login with username and password", description = "Login API")
    public ResponseEntity<Object> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO,
            BindingResult bindingResult) {
        // Validate request
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        // Create authentication token with username and password
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginRequestDTO.getUsername(), loginRequestDTO.getPassword());

        // Authenticate user
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // Set authentication to Security Context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        var userInformationDTO = authService.getUserInformationDTO(loginRequestDTO.getUsername());

        // Generate JWT token
        String accessToken = tokenService.generateToken(authentication, userInformationDTO);

        // create response
        LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
        loginResponseDTO.setAccessToken(accessToken);
        loginResponseDTO.setUser(userInformationDTO);

        return ResponseEntity.ok(loginResponseDTO);
    }

    // Register API
    @PostMapping("/register")
    @Operation(summary = "Register new account", description = "Register new account API")
    public ResponseEntity<Object> register(@Valid @RequestBody RegisterRequestDTO registerRequest,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        var result = authService.register(registerRequest);

        if (result) {
            var loginRequestDTO = new LoginRequestDTO();
            loginRequestDTO.setUsername(registerRequest.getUsername());
            loginRequestDTO.setPassword(registerRequest.getPassword());
            return login(loginRequestDTO, bindingResult);
        } else {
            return ResponseEntity.badRequest().body("Register failed");
        }
    }

    // Request reset password API
    @PostMapping("/request-reset-password")
    @Operation(summary = "Request reset password", description = "Request reset password API")
    public ResponseEntity<Object> requestResetPassword(
            @Valid @RequestBody ResetPasswordRequestDTO resetPasswordRequestDTO,
            BindingResult bindingResult) {
        // Validate request
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        // Request reset password
        var result = authService.requestResetPassword(resetPasswordRequestDTO);

        return ResponseEntity.ok().body(result);
    }

    // Validate reset password token API
    @PostMapping("/validate-reset-password-token")
    @Operation(summary = "Validate reset password token", description = "Validate reset password token API")
    public ResponseEntity<Object> validateResetPasswordToken(
            @Valid @RequestBody ValidateResetPasswordTokenRequestDTO requestDTO,
            BindingResult bindingResult) {
        // Validate request
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        // Validate reset password token
        var result = authService.validateResetPasswordToken(requestDTO);

        return ResponseEntity.ok().body(result);
    }

    // Change password API
    @PostMapping("/change-password")
    @Operation(summary = "Change password", description = "Change password API")
    public ResponseEntity<Object> changePassword(@Valid @RequestBody ChangePasswordRequestDTO changePasswordRequestDTO,
            BindingResult bindingResult) {
        // Validate request
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        // Change password
        var result = authService.changePassword(changePasswordRequestDTO);

        return ResponseEntity.ok().body(result);
    }
}
