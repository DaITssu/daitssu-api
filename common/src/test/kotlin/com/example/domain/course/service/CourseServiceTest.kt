package com.example.domain.course.service

import com.example.common.enums.RegisterStatus
import com.example.domain.course.model.repository.UserCourseRelationRepository
import com.example.domain.main.model.repository.UserRepository
import com.example.utils.IntegrationTest
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@IntegrationTest
class CourseServiceTest(
    private val courseService: CourseService,
    private val userRepository: UserRepository,
    private val userCourseRelationRepository: UserCourseRelationRepository
) {
    @Test
    @DisplayName("성공_올바른 userId를 이용하여 과목 조회 시_1개 이상의 과목이 조회될 수 있다")
    fun success_get_course_with_user_id() {
        userRepository.findAll().forEach { user ->
            val courses = userCourseRelationRepository.findByUserIdOrderByCreatedAtDesc(userId = user.id).filter {
                RegisterStatus.ACTIVE == it.registerStatus
            }

            val findCourses = courseService.getCourse(userId = user.id)

            assertAll(
                { assertThat(findCourses).isNotEmpty },
                { assertThat(findCourses.size).isEqualTo(courses.size) }
            )
        }
    }

    @Test
    @DisplayName("성공_잘못된 userId를 이용하여 과목 조회 시_빈 List를 받는다")
    fun success_get_empty_course_with_wrong_user_id() {
        val wrongUserId = 0L

        val findCourses = courseService.getCourse(userId = wrongUserId)

        assertAll(
            { assertThat(findCourses).isEmpty() }
        )
    }
}
