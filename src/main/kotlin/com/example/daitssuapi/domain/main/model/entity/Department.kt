package com.example.daitssuapi.domain.main.model.entity

import com.example.daitssuapi.common.audit.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(schema = "main")
class Department(
    var name: String
) : BaseEntity()
