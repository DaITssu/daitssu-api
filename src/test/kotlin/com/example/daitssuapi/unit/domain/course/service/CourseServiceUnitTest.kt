package com.example.daitssuapi.unit.domain.course.service

import com.example.daitssuapi.common.enums.RegisterStatus
import com.example.daitssuapi.domain.course.model.entity.Course
import com.example.daitssuapi.domain.course.model.entity.UserCourseRelation
import com.example.daitssuapi.domain.course.model.repository.*
import com.example.daitssuapi.domain.course.service.CourseService
import com.example.daitssuapi.domain.main.model.entity.Department
import com.example.daitssuapi.domain.main.model.entity.User
import com.example.daitssuapi.utils.UnitTest
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@UnitTest
class CourseServiceUnitTest {
    private val assignmentRepository: AssignmentRepository = mockk(relaxed = true)
    private val courseRepository: CourseRepository = mockk(relaxed = true)
    private val videoRepository: VideoRepository = mockk(relaxed = true)
    private val calendarRepository: CalendarRepository = mockk(relaxed = true)
    private val userCourseRelationRepository: UserCourseRelationRepository = mockk(relaxed = true)
    private val courseService: CourseService = spyk(
        objToCopy = CourseService(assignmentRepository, courseRepository, videoRepository, calendarRepository, userCourseRelationRepository),
        recordPrivateCalls = true
    )

    @Test
    @DisplayName("성공_올바른 userId를 이용하여 과목 조회 시_1개 이상의 과목이 조회될 수 있다")
    fun success_get_course_with_user_id() {
        val department = Department("computer")
        val user = User(studentId = 20230803, name = "user", department = department)
        val course = Course("eat anything", 16)
        every { userCourseRelationRepository.findByUserIdOrderByCreatedAtDesc(any()) }.returns(listOf(UserCourseRelation(user, course, RegisterStatus.ACTIVE)))

        val findCourses = courseService.getUserCourse(userId = user.id)

        assertAll(
            { assertThat(findCourses).isNotEmpty },
            { assertThat(findCourses.size).isEqualTo(1) }
        )
    }

    @Test
    @DisplayName("성공_잘못된 userId를 이용하여 과목 조회 시_빈 List를 받는다")
    fun success_get_empty_course_with_wrong_user_id() {
        val wrongUserId = 0L
        every { userCourseRelationRepository.findByUserIdOrderByCreatedAtDesc(any()) }.returns(emptyList())

        val findCourses = courseService.getUserCourse(userId = wrongUserId)

        assertAll(
            { assertThat(findCourses).isEmpty() }
        )
    }
}
