package com.example.domain.course.model.entity

import com.example.common.domain.BaseEntity
import com.example.common.enums.RegisterStatus
import com.example.domain.main.model.entity.User
import jakarta.persistence.*

@Entity
@Table(schema = "course")
class UserCourseRelation(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    val course: Course,

    @Enumerated(EnumType.STRING)
    val registerStatus: RegisterStatus
) : BaseEntity()
