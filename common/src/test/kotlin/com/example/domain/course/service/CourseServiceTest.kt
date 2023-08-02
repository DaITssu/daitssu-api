package com.example.domain.course.service

import com.example.common.enums.RegisterStatus
import com.example.domain.course.model.repository.UserCourseRelationRepository
import com.example.domain.main.model.repository.UserRepository
import com.example.utils.IntegrationTest
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.test.context.jdbc.Sql

@Sql("classpath:/test.sql")
@IntegrationTest
class CourseServiceTest(
    private val courseService: CourseService,
    private val userRepository: UserRepository,
    private val userCourseRelationRepository: UserCourseRelationRepository
) {
    @Test
    @DisplayName("성공_정상적인 과목 조회 시_user_id를 이용하여 과목을 가져온다")
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
