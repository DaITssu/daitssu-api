package com.example.domain.course.service

import com.example.common.enums.ErrorCode
import com.example.common.enums.RegisterStatus
import com.example.common.exception.DefaultException
import com.example.domain.course.dto.response.CourseResponse
import com.example.domain.course.model.repository.UserCourseRelationRepository
import org.springframework.stereotype.Service

@Service
class CourseService(
    private val userCourseRelationRepository: UserCourseRelationRepository
) {
    fun getCourse(courseId: Long, userId: Long): CourseResponse {
        val courses = userCourseRelationRepository.findByUserId(userId = userId).filter {
            RegisterStatus.ACTIVE == it.registerStatus
        }.map {
            it.course
        }.sortedByDescending { it.createdAt }

        if (courses.isEmpty()) {
            throw DefaultException(errorCode = ErrorCode.BAD_REQUEST)
        }

        return CourseResponse(courses = courses)
    }
}
