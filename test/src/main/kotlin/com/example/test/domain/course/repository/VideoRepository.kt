package com.example.test.domain.course.repository

import com.example.test.domain.course.entity.Video
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface VideoRepository : JpaRepository<Video, Long> {
    fun findByCourseId(courseId: Long): List<Video>
}