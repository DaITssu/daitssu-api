package com.example.domain.main.model.entity

import com.example.common.domain.BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "course")
class Course (
    @Column(name = "name", nullable = false)
    val name: String,
    
    @Column(name = "term", nullable = false)
    val term: Int,
    
    @OneToMany(mappedBy = "course")
    val videos: MutableList<Video> = mutableListOf(),
    
    @OneToMany(mappedBy = "course")
    val assignments: MutableList<Assignment> = mutableListOf()
) : BaseEntity() {
    fun addVideo(video: Video) {
        videos.add(video)
        video.course = this
    }
    
    fun addAssignment(assignment: Assignment) {
        assignments.add(assignment)
        assignment.course = this
    }
    
}
