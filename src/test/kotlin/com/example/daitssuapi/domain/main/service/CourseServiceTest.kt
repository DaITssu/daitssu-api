package com.example.daitssuapi.domain.main.service

import com.example.daitssuapi.common.enums.RegisterStatus
import com.example.daitssuapi.domain.course.model.repository.UserCourseRelationRepository
import com.example.daitssuapi.domain.course.service.CourseService
import com.example.daitssuapi.utils.IntegrationTest
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@IntegrationTest
class CourseServiceTest(
    private val courseService: CourseService,
    private val userRepository: UserCourseRelationRepository,
    private val userCourseRelationRepository: UserCourseRelationRepository
) {
    @Test
    @DisplayName("성공_올바른 userId를 이용하여 과목 조회 시_1개 이상의 과목이 조회될 수 있다")
    fun success_get_course_with_user_id() {
        userCourseRelationRepository.findAll().map { it.user.id }.forEach { userId ->
            val courses = userCourseRelationRepository.findByUserIdOrderByCreatedAtDesc(userId = userId).filter {
                RegisterStatus.ACTIVE == it.registerStatus
            }

            val findCourses = courseService.getUserCourses(userId = userId)

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

        val findCourses = courseService.getUserCourses(userId = wrongUserId)

        assertAll(
            { assertThat(findCourses).isEmpty() }
        )
    }
}
