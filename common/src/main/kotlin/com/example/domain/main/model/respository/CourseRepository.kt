package com.example.domain.main.model.respository

import com.example.domain.main.model.entity.Course
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CourseRepository : JpaRepository<Course, Long> {
}