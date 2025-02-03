package com.maiphong.insightcommerce.services.security;

import org.springframework.security.core.Authentication;

import com.maiphong.insightcommerce.dtos.security.user.UserInformationDTO;

public interface ITokenService {

    String generateToken(Authentication authentication, UserInformationDTO userInformationDTO);

    Authentication getAuthentication(String jwtToken);

    UserInformationDTO getUserInformation(String jwtToken);

}
