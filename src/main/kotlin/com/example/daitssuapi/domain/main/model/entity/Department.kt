package com.example.daitssuapi.domain.main.model.entity

import com.example.daitssuapi.common.audit.BaseEntity
import jakarta.persistence.Entity

@Entity
class Department(
    var name: String
) : BaseEntity()
