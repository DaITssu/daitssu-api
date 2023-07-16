package com.example.domain.main.model.entity

import com.example.common.domain.entity.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(schema = "main")
class Department(
    val name: String
) : BaseEntity()
