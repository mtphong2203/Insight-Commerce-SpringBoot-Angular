package com.maiphong.insightcommerce.services.security;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;

import com.maiphong.insightcommerce.dtos.security.user.ChangeAvatarDTO;
import com.maiphong.insightcommerce.dtos.security.user.ChangePasswordDTO;
import com.maiphong.insightcommerce.dtos.security.user.ChangeStatusDTO;
import com.maiphong.insightcommerce.dtos.security.user.ProfileUpdateDTO;
import com.maiphong.insightcommerce.dtos.security.user.UserBaseDTO;
import com.maiphong.insightcommerce.dtos.security.user.UserCreateUpdateDTO;
import com.maiphong.insightcommerce.dtos.security.user.UserInformationDTO;
import com.maiphong.insightcommerce.dtos.security.user.UserMasterDTO;
import com.maiphong.insightcommerce.dtos.security.user.UserSearchDTO;
import com.maiphong.insightcommerce.entities.security.User;
import com.maiphong.insightcommerce.mappers.IUserMapper;
import com.maiphong.insightcommerce.repositories.security.IUserRepository;

public class UserService implements IUserService {

    private final IUserRepository userRepository;
    private final IUserMapper userMapper;

    public UserService(IUserRepository userRepository, IUserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public List<UserBaseDTO> getAll() {
        List<User> users = userRepository.findAll();

        return users.stream().map(userMapper::toBaseDTO).toList();
    }

    @Override
    public List<UserMasterDTO> search(String keyword) {
        Specification<User> spec = buildKeywordSpecification(keyword);

        List<User> users = userRepository.findAll(spec);

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
                cb.like(cb.lower(root.get("username")), "%" + keyword.toLowerCase() + "%"),
                cb.like(cb.lower(root.get("email")), "%" + keyword.toLowerCase() + "%"),
                cb.like(cb.lower(root.get("phoneNumber")), "%" + keyword.toLowerCase() + "%"));

    }

    @Override
    public Page<UserMasterDTO> searchPaginated(UserSearchDTO userSearchDTO) {
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'create'");
    }

    @Override
    public UserMasterDTO update(UUID id, UserCreateUpdateDTO userDTO) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public boolean delete(UUID id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public UserInformationDTO updateProfile(ProfileUpdateDTO request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateProfile'");
    }

    @Override
    public boolean changePassword(ChangePasswordDTO request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'changePassword'");
    }

    @Override
    public boolean toggleActiveStatus(UUID id, ChangeStatusDTO request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toggleActiveStatus'");
    }

    @Override
    public boolean changeAvatar(ChangeAvatarDTO request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'changeAvatar'");
    }

}
