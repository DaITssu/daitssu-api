package com.example.domain.course.model.repository

import com.example.domain.course.model.entity.Assignment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AssignmentRepository : JpaRepository<Assignment, Long> {
    
    fun findByCourseId(courseId: Long): List<Assignment>
}