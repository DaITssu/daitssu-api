package com.example.daitssuapi.domain.user.model.entity

import com.example.daitssuapi.common.audit.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.hibernate.annotations.Where

@Entity
@Table(name = "users")
@Where(clause = "is_deleted = 0")
class User(
    val studentId: String,

    val name: String,

    var nickname: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    val department: Department,

    var imageUrl: String? = null,

    var term: Int,

    var ssuToken: String?,

    var refreshToken: String,
    
    var isDeleted: Boolean = false
) : BaseEntity()
