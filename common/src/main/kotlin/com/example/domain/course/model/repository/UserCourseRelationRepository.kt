package com.example.domain.course.model.repository

import com.example.domain.course.model.entity.UserCourseRelation
import org.springframework.data.jpa.repository.JpaRepository

interface UserCourseRelationRepository : JpaRepository<UserCourseRelation, Long> {
    fun findByUserId(userId: Long): List<UserCourseRelation>
}
