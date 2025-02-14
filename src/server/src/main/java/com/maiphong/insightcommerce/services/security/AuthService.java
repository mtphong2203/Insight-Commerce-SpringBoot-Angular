package com.maiphong.insightcommerce.services.security;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.maiphong.insightcommerce.core.constants.CommonConstant;
import com.maiphong.insightcommerce.dtos.email.EmailRequestDTO;
import com.maiphong.insightcommerce.dtos.security.auth.ChangePasswordRequestDTO;
import com.maiphong.insightcommerce.dtos.security.auth.RegisterRequestDTO;
import com.maiphong.insightcommerce.dtos.security.auth.ResetPasswordRequestDTO;
import com.maiphong.insightcommerce.dtos.security.auth.ValidateResetPasswordTokenRequestDTO;
import com.maiphong.insightcommerce.dtos.security.user.UserInformationDTO;
import com.maiphong.insightcommerce.entities.security.User;
import com.maiphong.insightcommerce.exceptions.ResourceNotFoundException;
import com.maiphong.insightcommerce.mappers.IUserMapper;
import com.maiphong.insightcommerce.repositories.security.IRoleRepository;
import com.maiphong.insightcommerce.repositories.security.IUserRepository;
import com.maiphong.insightcommerce.services.IEmailService;

@Service
@Transactional
public class AuthService implements IAuthService, UserDetailsService {

    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;
    private final IUserMapper userMapper;
    private final IPasswordService passwordService;
    private final IEmailService emailService;

    @Value("${commerce.email.template.reset-password.name}")
    private String resetPasswordEmailTemplateName;

    @Value("${commerce.frontend.url}")
    private String frontendUrl;

    public AuthService(
            IUserRepository userRepository,
            IRoleRepository roleRepository,
            IUserMapper userMapper,
            IPasswordService passwordService,
            IEmailService emailService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
        this.passwordService = passwordService;
        this.emailService = emailService;
    }

    // Trien khai login
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Find user by username and password
        User user = userRepository.findByUsername(username).orElse(null);

        if (user == null) {
            throw new UsernameNotFoundException(CommonConstant.USER_NOT_FOUND);
        }

        Set<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> CommonConstant.ROLE_PREFIX + role.getName())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                authorities);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public UserInformationDTO getUserInformationDTO(String username) {
        var currentUser = userRepository.findByUsername(username).orElse(null);

        if (currentUser == null) {
            throw new ResourceNotFoundException(CommonConstant.USER_NOT_FOUND);
        }

        return userMapper.toInformationDTO(currentUser);
    }

    @Override
    public boolean requestResetPassword(ResetPasswordRequestDTO request) {
        String email = request.getEmail();
        User user = userRepository.findByEmailNative(email).orElse(null);
        if (user == null) {
            throw new UsernameNotFoundException(CommonConstant.USER_NOT_FOUND);
        }

        String token = passwordService.generatePasswordResetToken(request.getEmail());

        // Send email with token to user
        EmailRequestDTO emailRequest = new EmailRequestDTO();
        emailRequest.setTo(request.getEmail());
        emailRequest.setSubject("Reset password");
        emailRequest.setTemplateName(resetPasswordEmailTemplateName);

        Map<String, Object> model = Map.of(
                "email", user.getEmail(),
                "resetLink", frontendUrl + "/reset-password?token=" + token);
        emailRequest.setVariables(model);

        emailService.sendEmailAsync(emailRequest);

        return true;
    }

    @Override
    public boolean validateResetPasswordToken(ValidateResetPasswordTokenRequestDTO request) {
        return passwordService.validatePasswordResetToken(request.getToken());
    }

    @Override
    public boolean changePassword(ChangePasswordRequestDTO request) {
        String username = passwordService.getEmailFromPasswordResetToken(request.getToken());
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            throw new UsernameNotFoundException(CommonConstant.USER_NOT_FOUND);
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("New password and confirm password are not matched");
        }

        // Change password
        user.setPassword(passwordService.hashPassword(request.getNewPassword()));
        userRepository.save(user);

        // Delete password reset token
        return passwordService.deletePasswordResetToken(request.getToken());
    }

    @Override
    public boolean register(RegisterRequestDTO request) {
        if (request == null) {
            throw new IllegalArgumentException("Request is null");
        }

        var existingUser = userRepository.findByUsernameOrEmail(request.getUsername(), request.getEmail()).orElse(null);

        if (existingUser != null && existingUser.getUsername().equals(request.getUsername())) {
            throw new IllegalArgumentException("Username already in use");
        }

        if (existingUser != null && existingUser.getEmail().equals(request.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        User user = userMapper.toEntity(request);
        user.setPassword(passwordService.hashPassword(request.getPassword()));

        // Assign default role is Customer
        var role = roleRepository.findByName(CommonConstant.ROLE_CUSTOMER).orElse(null);

        if (role == null) {
            throw new ResourceNotFoundException(CommonConstant.ROLE_NOT_FOUND);
        }

        user.setRoles(Set.of());

        userRepository.save(user);

        return true;
    }

}
