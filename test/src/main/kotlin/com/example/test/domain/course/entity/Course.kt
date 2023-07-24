package com.example.test.domain.course.entity

import jakarta.persistence.*

@Entity
@Table(name = "course")
class Course (
    @Column(name = "name", nullable = false)
    val name: String,

    @Column(name = "term", nullable = false)
    val term: Int,
    
    @OneToMany(mappedBy = "course")
    private val videos: MutableList<Video> = mutableListOf(),
    
    @OneToMany(mappedBy = "course")
    private val assignments: MutableList<Assignment> = mutableListOf()
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id", nullable = false)
    val id: Long? = null
    
    
    fun addVideo(video: Video) {
        videos.add(video)
        video.course = this
    }
    
    fun addAssignment(assignment: Assignment) {
        assignments.add(assignment)
        assignment.course = this
    }
    
}
