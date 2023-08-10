package com.example.domain.course.service

import com.example.common.enums.RegisterStatus
import com.example.domain.course.dto.response.CourseResponse
import com.example.domain.course.model.repository.UserCourseRelationRepository
import org.springframework.stereotype.Service

@Service
class CourseService(
    private val userCourseRelationRepository: UserCourseRelationRepository
) {
    fun getCourse(userId: Long): List<CourseResponse> =
        userCourseRelationRepository.findByUserIdOrderByCreatedAtDesc(userId = userId).filter {
            RegisterStatus.ACTIVE == it.registerStatus
        }.map {
            CourseResponse(
                name = it.course.name,
                term = it.course.term,
                updatedAt = it.course.updatedAt
            )
        }
}
