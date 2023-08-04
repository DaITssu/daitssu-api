package com.example.domain.main.model.repository

import com.example.domain.main.model.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
    fun findByStudentId(studentId: Int): User?
}