package com.example.common.domain.user.repository

import com.example.common.domain.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findByEmail(email: String): User?

    fun existsUserByEmail(email: String): Boolean
}
