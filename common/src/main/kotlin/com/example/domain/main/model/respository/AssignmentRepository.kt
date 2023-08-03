package com.example.domain.main.model.respository

import com.example.domain.main.model.entity.Assignment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AssignmentRepository : JpaRepository<Assignment, Long> {
    
    fun findByCourseId(courseId: Long): List<Assignment?>
}