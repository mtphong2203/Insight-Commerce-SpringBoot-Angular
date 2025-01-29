package com.maiphong.insightcommerce.entities;

import java.util.UUID;

import jakarta.persistence.*;

import lombok.*;

@Getter
@Setter
@MappedSuperclass
public class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
}
