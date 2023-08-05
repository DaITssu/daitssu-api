package com.example.domain.main.model.respository

import com.example.domain.main.model.entity.Video
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface VideoRepository : JpaRepository<Video, Long> {
    fun findByCourseId(courseId: Long): List<Video?>
}