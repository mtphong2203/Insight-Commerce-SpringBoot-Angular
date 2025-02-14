package com.maiphong.insightcommerce.repositories.security;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.maiphong.insightcommerce.entities.security.User;

public interface IUserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {

        @Transactional(propagation = Propagation.REQUIRES_NEW)
        Optional<User> findByUsername(String username);

        Optional<User> findByEmail(String email);

        @Query(value = "SELECT * FROM users WHERE email = :email", nativeQuery = true)
        Optional<User> findByEmailNative(@Param("email") String email);

        Optional<User> findByUsernameOrEmail(String username, String email);

        Optional<User> findByEmailOrPhoneNumber(String email, String phoneNumber);

        Boolean existsByUsername(String username);

        Boolean existsByEmail(String email);

        @Query("SELECT MAX(CAST(SUBSTRING(u.username, LENGTH(:baseUsername) + 1, LENGTH(u.username) - LENGTH(:baseUsername)) AS int)) "
                        + "FROM User u WHERE u.username LIKE CONCAT(:baseUsername, '%') AND "
                        + "SUBSTRING(u.username, LENGTH(:baseUsername) + 1, LENGTH(u.username) - LENGTH(:baseUsername)) NOT LIKE '%[^0-9]%'")
        Integer findMaxNumberForUsername(@Param("baseUsername") String baseUsername);

        @Query("SELECT MAX(CAST(SUBSTRING(u.username, LENGTH(:baseUsername) + 1, LENGTH(u.username) - LENGTH(:baseUsername)) AS int)) "
                        + "FROM User u WHERE u.id <> :id AND u.username LIKE CONCAT(:baseUsername, '%') AND "
                        + "SUBSTRING(u.username, LENGTH(:baseUsername) + 1, LENGTH(u.username) - LENGTH(:baseUsername)) NOT LIKE '%[^0-9]%'")
        Integer findMaxNumberForUsernameAndId(@Param("baseUsername") String baseUsername, @Param("id") UUID id);

}
