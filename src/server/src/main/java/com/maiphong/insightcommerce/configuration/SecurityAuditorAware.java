package com.maiphong.insightcommerce.configuration;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.maiphong.insightcommerce.entities.security.User;
import com.maiphong.insightcommerce.repositories.security.IUserRepository;

public class SecurityAuditorAware implements AuditorAware<User> {
    private final IUserRepository userRepository;

    public SecurityAuditorAware(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() ||
                authentication.getPrincipal().equals("anonymousUser")) {
            return Optional.empty();
        }

        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElse(null);
        return Optional.ofNullable(user);
    }
}
