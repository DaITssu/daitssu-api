package com.example.common.domain.user.entity

import com.example.common.converter.UserRoleConverter
import com.example.common.entitybase.AuditLoggingBase
import com.example.common.enums.UserRole
import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

@Entity
@Table(name = "users")
class User(
    @Column(name = "email", nullable = false)
    val email: String,

    @Column(name = "password", nullable = false)
    val password: String,

    @Column(name = "username", nullable = false)
    val username: String,

    @Size(max = 255)
    @NotNull
    @Column(name = "user_role", nullable = false)
    @Convert(converter = UserRoleConverter::class)
    val userRole: UserRole,
) : AuditLoggingBase() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    val id: Long? = null
}
