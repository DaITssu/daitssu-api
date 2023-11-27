package com.example.daitssuapi.domain.main.model.entity

import com.example.daitssuapi.common.audit.BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "users")
class User(
    val studentId: Long,

    val name: String,

    var nickname: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    val department: Department,

    var imageUrl: String? = null,

    var term: Int,
    
    var ssuToken: String?,

    var refreshToken: String,
) : BaseEntity()
