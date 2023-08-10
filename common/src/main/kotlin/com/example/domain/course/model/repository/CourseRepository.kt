package com.example.domain.course.model.repository

import com.example.domain.course.model.entity.Course
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CourseRepository : JpaRepository<Course, Long> {
}