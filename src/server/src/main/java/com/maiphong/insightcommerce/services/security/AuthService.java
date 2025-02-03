package com.maiphong.insightcommerce.services.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.maiphong.insightcommerce.dtos.security.auth.ChangePasswordRequestDTO;
import com.maiphong.insightcommerce.dtos.security.auth.ResetPasswordRequestDTO;
import com.maiphong.insightcommerce.dtos.security.auth.ValidateResetPasswordTokenRequestDTO;
import com.maiphong.insightcommerce.dtos.security.user.UserInformationDTO;

public class AuthService implements IAuthService, UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'loadUserByUsername'");
    }

    @Override
    public boolean existsByUsername(String username) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'existsByUsername'");
    }

    @Override
    public UserInformationDTO getUserInformationDTO(String username) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getUserInformationDTO'");
    }

    @Override
    public boolean requestResetPassword(ResetPasswordRequestDTO request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'requestResetPassword'");
    }

    @Override
    public boolean validateResetPasswordToken(ValidateResetPasswordTokenRequestDTO request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'validateResetPasswordToken'");
    }

    @Override
    public boolean changePassword(ChangePasswordRequestDTO request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'changePassword'");
    }

}
