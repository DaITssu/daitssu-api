package com.example.daitssuapi.domain.course.model.entity

import com.example.daitssuapi.common.audit.BaseEntity
import com.example.daitssuapi.common.enums.RegisterStatus
import com.example.daitssuapi.domain.user.model.entity.User
import jakarta.persistence.*

@Entity
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
