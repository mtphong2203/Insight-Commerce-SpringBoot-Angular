package com.maiphong.insightcommerce.entities.security;

import java.time.ZonedDateTime;

import org.hibernate.annotations.TimeZoneStorage;
import org.hibernate.annotations.TimeZoneStorageType;

import com.maiphong.insightcommerce.entities.BaseEntity;

import jakarta.persistence.*;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "password_reset_tokens", indexes = {
        @Index(name = "idx_password_reset_tokens_email", columnList = "email")
})
public class PasswordResetToken extends BaseEntity {
    @Column(nullable = false, unique = true)
    private String hashedTokenId;

    @Column(nullable = false)
    private String email;

    @TimeZoneStorage(TimeZoneStorageType.NATIVE)
    @Column(columnDefinition = "DATETIMEOFFSET")
    private ZonedDateTime expiration;
}
