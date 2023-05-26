package com.example.test.configuration

import com.example.common.domain.user.entity.User
import com.example.common.domain.user.repository.UserRepository
import com.example.common.exception.UserNotFoundException
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component

@Component
class CustomUserDetailsService(
    private val userRepository: UserRepository
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByEmail(username) ?: throw UserNotFoundException()
        return createUserDetails(user = user)
    }

    private fun createUserDetails(user: User): org.springframework.security.core.userdetails.User {
        return org.springframework.security.core.userdetails.User(
            user.email,
            user.password,
            listOf(SimpleGrantedAuthority(user.userRole.role))
        )
    }
}
