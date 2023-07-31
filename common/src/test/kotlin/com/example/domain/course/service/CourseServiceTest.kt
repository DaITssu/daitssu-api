package com.example.domain.course.service

import com.example.common.enums.RegisterStatus
import com.example.domain.course.model.repository.UserCourseRelationRepository
import com.example.domain.main.model.repository.UserRepository
import jakarta.transaction.Transactional
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestConstructor
import org.springframework.test.context.jdbc.Sql

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@ActiveProfiles("local")
@Sql("classpath:/test.sql")
@Transactional
class CourseServiceTest(
    private val courseService: CourseService,
    private val userRepository: UserRepository,
    private val userCourseRelationRepository: UserCourseRelationRepository
) {
    @Test
    @DisplayName("성공 - user_id를 이용하여 과목을 가져온다")
    fun success_get_course_with_user_id() {
        userRepository.findAll().forEach { user ->
            val courses = userCourseRelationRepository.findByUserId(userId = user.id).filter {
                RegisterStatus.ACTIVE == it.registerStatus
            }

            val findCourses = courseService.getCourse(userId = user.id).courses

            assertAll(
                { assertThat(findCourses.size).isEqualTo(courses.size) }
            )
        }
    }
}
