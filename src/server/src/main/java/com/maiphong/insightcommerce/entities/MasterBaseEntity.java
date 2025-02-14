package com.maiphong.insightcommerce.entities;

import java.time.ZonedDateTime;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.TimeZoneStorage;
import org.hibernate.annotations.TimeZoneStorageType;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.maiphong.insightcommerce.core.constants.CommonConstant;
import com.maiphong.insightcommerce.entities.security.User;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@FilterDef(name = CommonConstant.DELETED_FILTER, defaultCondition = "deleted_at IS NULL")
@Filter(name = CommonConstant.DELETED_FILTER)
public class MasterBaseEntity extends BaseEntity {

    @TimeZoneStorage(TimeZoneStorageType.NATIVE)
    @Column(name = "inserted_at", columnDefinition = "DATETIMEOFFSET", nullable = false, updatable = false)
    private ZonedDateTime insertedAt;

    @CreatedBy
    @ManyToOne
    @JoinColumn(name = "inserted_by", updatable = false)
    private User insertedBy;

    @TimeZoneStorage(TimeZoneStorageType.NATIVE)
    @Column(name = "updated_at", columnDefinition = "DATETIMEOFFSET")
    private ZonedDateTime updatedAt;

    @LastModifiedBy
    @ManyToOne
    @JoinColumn(name = "updated_by")
    private User updatedBy;

    @TimeZoneStorage(TimeZoneStorageType.NATIVE)
    @Column(name = "deleted_at", columnDefinition = "DATETIMEOFFSET")
    private ZonedDateTime deletedAt;

    @LastModifiedBy
    @ManyToOne
    @JoinColumn(name = "deleted_by")
    private User deletedBy;

    @PrePersist
    public void prePersist() {
        this.insertedAt = ZonedDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = ZonedDateTime.now();
    }

    @PreRemove
    public void preRemove() {
        // For soft delete , set deletedAt but not delete to database
        if (this.deletedAt == null) {
            this.deletedAt = ZonedDateTime.now();
            this.deletedBy = this.updatedBy;
        }
    }
}
