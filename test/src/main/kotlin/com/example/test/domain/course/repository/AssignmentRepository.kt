package com.example.test.domain.course.repository

import com.example.test.domain.course.entity.Assignment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AssignmentRepository : JpaRepository<Assignment, Long> {
    
    fun findByCourseId(courseId: Long): List<Assignment>
}