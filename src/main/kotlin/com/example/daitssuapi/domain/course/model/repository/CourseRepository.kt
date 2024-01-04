package com.example.daitssuapi.domain.course.model.repository

import com.example.daitssuapi.domain.course.model.entity.Course
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CourseRepository : JpaRepository<Course, Long> {
    fun findByName(name: String) : Course?
    
}