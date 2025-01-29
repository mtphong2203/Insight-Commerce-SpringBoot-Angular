package com.maiphong.insightcommerce.repositories.security;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.maiphong.insightcommerce.entities.security.User;

public interface IUserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {
    Optional<User> findByUsername(String username);

    User findByEmail(String email);

    User findByUsernameOrEmail(String username, String email);

    User findByEmailOrPhoneNumber(String email, String phoneNumber);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    @Query("SELECT MAX(CAST(SUBSTRING(u.username, LENGTH(:baseUsername) + 1, LENGTH(u.username) - LENGTH(:baseUsername)) AS int)) "
            +
            "FROM User u WHERE u.username LIKE CONCAT(:baseUsername, '%') AND " +
            "SUBSTRING(u.username, LENGTH(:baseUsername) + 1, LENGTH(u.username) - LENGTH(:baseUsername)) NOT LIKE '%[^0-9]%'")
    Integer findMaxNumberForUsername(@Param("baseUserName") String baseUserName);

    @Query("SELECT MAX(CAST(SUBSTRING(u.username, LENGTH(:baseUsername) + 1, LENGTH(u.username) - LENGTH(:baseUsername)) AS int)) "
            +
            "FROM User u WHERE u.id <> :id AND u.username LIKE CONCAT(:baseUsername, '%') AND " +
            "SUBSTRING(u.username, LENGTH(:baseUsername) + 1, LENGTH(u.username) - LENGTH(:baseUsername)) NOT LIKE '%[^0-9]%'")
    Integer findMaxNumberForUsernameAndId(@Param("baseUsername") String baseUsername, @Param("id") UUID id);

}
