package com.example.daitssuapi.domain.course.model.repository

import com.example.daitssuapi.domain.course.model.entity.UserCourseRelation
import org.springframework.data.jpa.repository.JpaRepository

interface UserCourseRelationRepository : JpaRepository<UserCourseRelation, Long> {
    fun findByUserIdOrderByCreatedAtDesc(userId: Long): List<UserCourseRelation>
}
