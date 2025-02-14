package com.maiphong.insightcommerce.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;

import com.maiphong.insightcommerce.entities.security.User;
import com.maiphong.insightcommerce.repositories.security.IUserRepository;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "securityAuditorAware")
public class AuditingConfig {
    private final IUserRepository userRepository;

    @Autowired
    public AuditingConfig(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    public AuditorAware<User> securityAuditorAware() {
        return new SecurityAuditorAware(userRepository);
    }
}
