package com.maiphong.insightcommerce.services.security;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;

import com.maiphong.insightcommerce.dtos.security.user.ChangeAvatarDTO;
import com.maiphong.insightcommerce.dtos.security.user.ChangePasswordDTO;
import com.maiphong.insightcommerce.dtos.security.user.ChangeStatusDTO;
import com.maiphong.insightcommerce.dtos.security.user.ProfileUpdateDTO;
import com.maiphong.insightcommerce.dtos.security.user.UserBaseDTO;
import com.maiphong.insightcommerce.dtos.security.user.UserCreateUpdateDTO;
import com.maiphong.insightcommerce.dtos.security.user.UserInformationDTO;
import com.maiphong.insightcommerce.dtos.security.user.UserMasterDTO;
import com.maiphong.insightcommerce.dtos.security.user.UserSearchDTO;

public interface IUserService {
    List<UserBaseDTO> getAll();

    List<UserMasterDTO> search(String keyword);

    Page<UserMasterDTO> search(UserSearchDTO userSearchDTO);

    UserMasterDTO getById(String id);

    UserMasterDTO create(UserCreateUpdateDTO userDTO);

    UserMasterDTO update(UUID id, UserCreateUpdateDTO userDTO);

    boolean delete(UUID id, boolean hardDelete);

    UserInformationDTO updateProfile(ProfileUpdateDTO request);

    boolean changePassword(ChangePasswordDTO request);

    boolean toggleActiveStatus(UUID id, ChangeStatusDTO request);

    boolean changeAvatar(ChangeAvatarDTO request);

}
