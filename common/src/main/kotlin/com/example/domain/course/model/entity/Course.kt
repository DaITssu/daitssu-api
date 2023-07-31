package com.example.domain.course.model.entity

import com.example.common.domain.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(schema = "course")
class Course(
    val name: String,
    val term: Int
) : BaseEntity()
