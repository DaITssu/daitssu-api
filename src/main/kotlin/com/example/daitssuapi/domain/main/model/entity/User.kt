package com.example.daitssuapi.domain.main.model.entity

import com.example.daitssuapi.common.audit.BaseEntity
import com.example.daitssuapi.domain.main.model.entity.Department
import jakarta.persistence.*

@Entity
@Table(schema = "main", name = "users")
class User(
    val studentId: Int,

    val name: String,

    val nickname: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    val department: Department,

    val imageUrl: String? = null
) : BaseEntity()