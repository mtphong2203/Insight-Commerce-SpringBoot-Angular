package com.maiphong.insightcommerce.repositories.security;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.maiphong.insightcommerce.entities.security.PasswordResetToken;

public interface IPasswordResetTokenRepository extends JpaRepository<PasswordResetToken, UUID> {
    Optional<PasswordResetToken> findByHashedTokenId(String hashedTokenId);

    Optional<PasswordResetToken> findByEmail(String email);

    void deleteByHashedTokenId(String hashedTokenId);
}
