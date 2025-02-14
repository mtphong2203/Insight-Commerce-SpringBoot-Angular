package com.maiphong.insightcommerce.repositories.security;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.maiphong.insightcommerce.entities.security.Role;

public interface IRoleRepository extends JpaRepository<Role, UUID>, JpaSpecificationExecutor<Role> {

    @Query("SELECT r FROM Role r WHERE r.name = :name AND r.deletedAt IS NULL")
    Optional<Role> findByName(@Param("name") String name);

    Boolean existsByName(String name);
}
