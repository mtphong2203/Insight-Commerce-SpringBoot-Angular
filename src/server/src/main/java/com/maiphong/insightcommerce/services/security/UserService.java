package com.maiphong.insightcommerce.services.security;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.maiphong.insightcommerce.core.constants.CommonConstant;
import com.maiphong.insightcommerce.dtos.email.EmailRequestDTO;
import com.maiphong.insightcommerce.dtos.security.user.ChangeAvatarDTO;
import com.maiphong.insightcommerce.dtos.security.user.ChangePasswordDTO;
import com.maiphong.insightcommerce.dtos.security.user.ChangeStatusDTO;
import com.maiphong.insightcommerce.dtos.security.user.ProfileUpdateDTO;
import com.maiphong.insightcommerce.dtos.security.user.UserBaseDTO;
import com.maiphong.insightcommerce.dtos.security.user.UserCreateUpdateDTO;
import com.maiphong.insightcommerce.dtos.security.user.UserInformationDTO;
import com.maiphong.insightcommerce.dtos.security.user.UserMasterDTO;
import com.maiphong.insightcommerce.dtos.security.user.UserSearchDTO;
import com.maiphong.insightcommerce.entities.security.Role;
import com.maiphong.insightcommerce.entities.security.User;
import com.maiphong.insightcommerce.exceptions.EntityCreateUpdateException;
import com.maiphong.insightcommerce.exceptions.ResourceNotFoundException;
import com.maiphong.insightcommerce.mappers.IUserMapper;
import com.maiphong.insightcommerce.repositories.security.IRoleRepository;
import com.maiphong.insightcommerce.repositories.security.IUserRepository;
import com.maiphong.insightcommerce.services.IEmailService;
import com.maiphong.insightcommerce.utils.UserHelpers;

@Service
@Transactional
public class UserService implements IUserService {

    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;
    private final IUserMapper userMapper;
    private final IPasswordService passwordService;
    private final IEmailService emailService;

    @Value("${commerce.common.password.length}")
    private int passwordLength;

    @Value("${commerce.email.template.account-information.name}")
    private String accountInformationTemplate;

    // Define the constant for "username"
    private static final String USERNAME_FIELD = "username";

    public UserService(
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

    @Override
    public List<UserBaseDTO> getAll() {
        List<User> users = userRepository.findAll();

        return users.stream().map(userMapper::toBaseDTO).toList();
    }

    @Override
    public List<UserMasterDTO> search(String keyword) {
        // Build specification dynamically
        Specification<User> spec = buildKeywordSpecification(keyword);

        // Fetch users using the specification
        List<User> users = userRepository.findAll(spec);

        // Map entities to DTOs
        return users.stream().map(userMapper::toMasterDTO).toList();
    }

    /**
     * Helper method to build a dynamic specification forkeyword filtering
     */
    private Specification<User> buildKeywordSpecification(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return Specification.where(null);
        }

        return (root, query, cb) -> cb.or(
                cb.like(cb.lower(root.get(USERNAME_FIELD)), "%" + keyword.toLowerCase() + "%"),
                cb.like(cb.lower(root.get("email")), "%" + keyword.toLowerCase() + "%"),
                cb.like(cb.lower(root.get("phoneNumber")), "%" + keyword.toLowerCase() + "%"));

    }

    @Override
    public Page<UserMasterDTO> search(UserSearchDTO userSearchDTO) {
        Specification<User> spec = buildKeywordSpecification(userSearchDTO.getKeyword());

        if (spec == null) {
            spec = Specification.where(null);
        }
        // Filter by role IDs
        if (userSearchDTO.getRoleIds() != null && !userSearchDTO.getRoleIds().isEmpty()) {
            spec = spec.and((root, query, cb) -> root.join("roles").get("id").in(userSearchDTO.getRoleIds()));
        }
        // Filter by active
        if (userSearchDTO.getActive() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("isActive"), userSearchDTO.getActive()));
        }

        // Filter by gender
        if (userSearchDTO.getGender() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("gender"), userSearchDTO.getGender()));
        }

        var entities = userRepository.findAll(spec, userSearchDTO.toPageable());

        return entities.map(userMapper::toMasterDTO);
    }

    @Override
    public UserMasterDTO getById(String id) {
        User user = userRepository.findById(UUID.fromString(id)).orElse(null);

        if (user == null) {
            return null;
        }

        return userMapper.toMasterDTO(user);
    }

    @Override
    public UserMasterDTO create(UserCreateUpdateDTO userDTO) {
        if (userDTO == null) {
            throw new IllegalArgumentException("User is null");
        }

        var existingUser = userRepository.findByEmailOrPhoneNumber(userDTO.getEmail(), userDTO.getPhoneNumber())
                .orElse(null);
        if (existingUser != null) {
            if (existingUser.getEmail().equals(userDTO.getEmail())) {
                throw new IllegalArgumentException("User with email already exists");
            } else {
                throw new IllegalArgumentException("User with phone number already exists");
            }
        }

        // Check role exists
        var roles = roleRepository.findAllById(userDTO.getRoleIds());
        if (roles.size() != userDTO.getRoleIds().size()) {
            throw new IllegalArgumentException("Role not found");
        }

        var user = userMapper.toEntity(userDTO);
        String password = passwordService.generatePassword(passwordLength);
        user.setPassword(passwordService.hashPassword(password));

        String baseUsername = UserHelpers.generateUsername(userDTO.getFullName());

        // Tìm số lớn nhất hiện tại
        Integer maxNumber = userRepository.findMaxNumberForUsername(baseUsername);
        if (maxNumber != null) {
            maxNumber++;
            user.setUsername(baseUsername + maxNumber);
        } else {
            user.setUsername(baseUsername);
        }

        user.setRoles(Set.copyOf(roles));

        user = userRepository.save(user);

        // Check if user was saved
        boolean result = userRepository.existsByEmail(user.getEmail());

        if (!result) {
            throw new EntityCreateUpdateException("Failed to create user");
        }

        // Send email with password to user
        EmailRequestDTO emailRequest = new EmailRequestDTO();
        emailRequest.setTo(user.getEmail());
        emailRequest.setSubject("Welcome to IMS");
        emailRequest.setTemplateName(accountInformationTemplate);

        Map<String, Object> model = Map.of(
                USERNAME_FIELD, user.getUsername(),
                "password", password);

        emailRequest.setVariables(model);

        emailService.sendEmailAsync(emailRequest);

        return userMapper.toMasterDTO(user);
    }

    @Override
    public UserMasterDTO update(UUID id, UserCreateUpdateDTO request) {
        if (request == null) {
            throw new IllegalArgumentException("User is null");
        }

        var existingUser = userRepository.findByEmailOrPhoneNumber(request.getEmail(), request.getPhoneNumber())
                .orElse(null);

        if (existingUser != null && !existingUser.getId().equals(id)) {
            if (existingUser.getEmail().equals(request.getEmail())) {
                throw new IllegalArgumentException("User with email already exists");
            } else {
                throw new IllegalArgumentException("User with phone number already exists");
            }
        }

        var user = userRepository.findById(id).orElse(null);

        if (user == null) {
            throw new IllegalArgumentException(CommonConstant.USER_NOT_FOUND + request.getEmail());
        }

        // Check roles exist
        var roles = roleRepository.findAllById(request.getRoleIds());

        // Check if all roles exist
        if (roles.size() != request.getRoleIds().size()) {
            throw new IllegalArgumentException(CommonConstant.ROLE_NOT_FOUND);
        }

        // Create a mutable copy of the roles
        Set<Role> mutableRoles = new HashSet<>(user.getRoles());

        // Remove roles that are not in the new list
        mutableRoles.removeIf(role -> !roles.contains(role));

        // Update entity properties
        userMapper.updateEntity(request, user);
        user.setId(id);

        // Add new roles
        mutableRoles.addAll(roles);
        user.setRoles(mutableRoles);

        user = userRepository.save(user);

        return userMapper.toMasterDTO(user);
    }

    /**
     * Deletes a user either softly or hard based on the deleteType.
     *
     * @param id         UUID of the user to delete
     * @param hardDelete if true, perform hard delete; otherwise, perform soft
     *                   delete
     * @return boolean indicating success
     */
    @Override
    public boolean delete(UUID id, boolean hardDelete) {
        var user = userRepository.findById(id).orElse(null);

        if (user == null) {
            throw new ResourceNotFoundException(CommonConstant.USER_NOT_FOUND);
        }

        if (hardDelete) {
            userRepository.delete(user);
        } else {
            user.setDeletedAt(ZonedDateTime.now());
            userRepository.save(user);
        }

        return true;
    }

    @Override
    public UserInformationDTO updateProfile(ProfileUpdateDTO request) {
        // Retrieve current user ID from security context
        UserDetails currentUser = UserHelpers.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User is not authenticated");
        }

        // Fetch the user entity
        User existingUser = userRepository.findByUsername(currentUser.getUsername()).orElse(null);
        if (existingUser == null) {
            throw new ResourceNotFoundException(CommonConstant.USER_NOT_FOUND);
        }

        // Update user fields
        existingUser.setEmail(request.getEmail());
        existingUser.setPhoneNumber(request.getPhoneNumber());
        existingUser.setDateOfBirth(request.getDateOfBirth().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        existingUser.setAddress(request.getAddress());
        existingUser.setGender(request.isGender());

        if (!request.getFullName().equals(existingUser.getFullName())) {
            existingUser.setFullName(request.getFullName());

            // Update username
            String baseUsername = UserHelpers.generateUsername(request.getFullName());
            Integer maxNumber = userRepository.findMaxNumberForUsernameAndId(baseUsername, existingUser.getId());

            if (maxNumber != null) {
                maxNumber++;
                existingUser.setUsername(baseUsername + maxNumber);
            } else {
                existingUser.setUsername(baseUsername);
            }
        }

        // Save the updated user
        userRepository.save(existingUser);

        if (!currentUser.getUsername().equals(existingUser.getUsername())) {
            // Send email with password to user
            EmailRequestDTO emailRequest = new EmailRequestDTO();
            emailRequest.setTo(existingUser.getEmail());
            emailRequest.setSubject("Welcome to IMS");
            emailRequest.setTemplateName(accountInformationTemplate);

            Map<String, Object> model = Map.of(
                    USERNAME_FIELD, existingUser.getUsername(),
                    "password", "Your password is unchanged");

            emailRequest.setVariables(model);

            emailService.sendEmailAsync(emailRequest);
        }

        return userMapper.toInformationDTO(existingUser);
    }

    @Override
    public boolean changePassword(ChangePasswordDTO request) {
        // Retrieve current user ID from security context
        UserDetails currentUser = UserHelpers.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User is not authenticated");
        }

        // Fetch the user entity
        User user = userRepository.findByUsername(currentUser.getUsername()).orElse(null);
        if (user == null) {
            throw new ResourceNotFoundException(CommonConstant.USER_NOT_FOUND);
        }

        // Verify that the old password matches
        if (!passwordService.matchHashedPassword(request.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Old password is incorrect");
        }

        // Ensure new password and confirm password match
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("New password and confirm password do not match");
        }

        // Hash the new password and update
        String hashedNewPassword = passwordService.hashPassword(request.getNewPassword());
        user.setPassword(hashedNewPassword);

        // Save the updated user
        userRepository.save(user);

        return true;
    }

    @Override
    public boolean toggleActiveStatus(UUID id, ChangeStatusDTO request) {
        User user = userRepository.findById(id).orElse(null);

        if (user == null) {
            throw new ResourceNotFoundException(CommonConstant.USER_NOT_FOUND);
        }

        user.setActive(request.getStatus());
        userRepository.save(user);

        return true;
    }

    @Override
    public boolean changeAvatar(ChangeAvatarDTO request) {
        UserDetails currentUser = UserHelpers.getCurrentUser();
        User user = userRepository.findByUsername(currentUser.getUsername()).orElse(null);
        if (user == null) {
            throw new ResourceNotFoundException(CommonConstant.USER_NOT_FOUND);
        }

        if (request.getAvatarUrl() != null && !request.getAvatarUrl().isEmpty()) {
            user.setAvatar(request.getAvatarUrl());
            userRepository.save(user);
            return true;
        }

        throw new IllegalArgumentException("Invalid avatar URL");
    }

}
