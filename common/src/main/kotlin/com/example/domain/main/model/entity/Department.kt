package com.example.domain.main.model.entity

import com.example.common.domain.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(schema = "main")
class Department(
    var name: String
) : BaseEntity()