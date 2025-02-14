package com.maiphong.insightcommerce.services.security;

import com.maiphong.insightcommerce.dtos.security.auth.ChangePasswordRequestDTO;
import com.maiphong.insightcommerce.dtos.security.auth.RegisterRequestDTO;
import com.maiphong.insightcommerce.dtos.security.auth.ResetPasswordRequestDTO;
import com.maiphong.insightcommerce.dtos.security.auth.ValidateResetPasswordTokenRequestDTO;
import com.maiphong.insightcommerce.dtos.security.user.UserInformationDTO;

public interface IAuthService {
    boolean existsByUsername(String username);

    UserInformationDTO getUserInformationDTO(String username);

    boolean requestResetPassword(ResetPasswordRequestDTO request);

    boolean validateResetPasswordToken(ValidateResetPasswordTokenRequestDTO request);

    boolean changePassword(ChangePasswordRequestDTO request);

    boolean register(RegisterRequestDTO request);

}
