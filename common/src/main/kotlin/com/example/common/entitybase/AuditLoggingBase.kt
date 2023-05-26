package com.example.common.entitybase

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import jakarta.validation.constraints.NotNull
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class AuditLoggingBase {
    @NotNull
    @Column(name = "created_at")
    @CreatedDate
    lateinit var createdAt: LocalDateTime

    @NotNull
    @Column(name = "updated_at")
    @LastModifiedDate
    lateinit var updatedAt: LocalDateTime
}

