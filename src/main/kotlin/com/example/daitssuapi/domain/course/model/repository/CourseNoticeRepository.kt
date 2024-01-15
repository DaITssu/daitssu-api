package com.example.daitssuapi.domain.course.model.repository

import com.example.daitssuapi.domain.course.model.entity.CourseNotice
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CourseNoticeRepository : JpaRepository<CourseNotice, Long> {
    fun findByCourseId(courseId: Long): List<CourseNotice>
}
