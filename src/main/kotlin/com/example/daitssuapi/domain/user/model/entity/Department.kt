package com.example.daitssuapi.domain.user.model.entity

import com.example.daitssuapi.common.audit.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "department")
class Department(
    var name: String
) : BaseEntity()
