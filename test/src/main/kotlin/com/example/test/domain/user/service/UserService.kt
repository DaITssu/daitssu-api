package com.example.test.domain.user.service

import com.example.common.domain.user.entity.User
import com.example.common.domain.user.repository.UserRepository
import com.example.common.dto.UserDto
import com.example.common.dto.mapstruct.UserMapStruct
import com.example.common.enums.UserRole
import com.example.common.exception.UserAlreadyExistException
import com.example.common.exception.UserNotFoundException
import com.example.common.security.component.CustomPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
    private val userMapStruct: UserMapStruct,
    private val passwordEncoder: CustomPasswordEncoder,
) {
    fun getByEmail(email: String) = userRepository.findByEmail(email)
        ?: throw UserNotFoundException()

    @Transactional
    fun createUser(
        email: String,
        password: String,
        username: String,
        role: UserRole
    ): UserDto {
        val check = userRepository.existsUserByEmail(
            email = email,
        )

        // 이미 존재하는 고객이면 Exception
        if (check) throw UserAlreadyExistException()

        val userToSave = User(
            email = email,
            password = passwordEncoder.encode(password),
            username = username,
            userRole = role,
        )

        val user = userRepository.save(userToSave)

        return userMapStruct.toDto(user)
    }
}