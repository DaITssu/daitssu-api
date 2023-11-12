package com.example.daitssuapi.domain.main.service

import com.example.daitssuapi.common.exception.DefaultException
import com.example.daitssuapi.domain.main.model.repository.UserRepository
import com.example.daitssuapi.utils.IntegrationTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

@IntegrationTest
class UserServiceTest(
    private val userRepository: UserRepository,
    private val userService: UserService
) {
    @Test
    @DisplayName("성공_올바른 userId를 이용하여 유저 조회 시_유저가 조회된다")
    fun success_get_course_with_user_id() {
        userRepository.findAll().filter { null != it.nickname }.forEach {
            val findUser = userService.getUser(userId = it.id)

            assertAll(
                { assertThat(findUser.studentId).isEqualTo(it.studentId) },
                { assertThat(findUser.name).isEqualTo(it.name) },
                { assertThat(findUser.nickname).isEqualTo(it.nickname) },
                { assertThat(findUser.departmentName).isEqualTo(it.department.name) },
                { assertThat(findUser.term).isEqualTo(it.term) }
            )
        }
    }

    @Test
    @DisplayName("실패_잘못된 userId를 이용하여 유저 조회 시_404를 받는다")
    fun success_get_empty_course_with_wrong_user_id() {
        val wrongUserId = 0L

        assertThrows<DefaultException> { userService.getUser(userId = wrongUserId) }
    }
    @Test
    @DisplayName("user 닉네임 변환 테스트")
    fun update_nickname_test(){
        val before = userRepository.findAll()[0]
        val beforeNickname = before.nickname
        val nickname = "test"
        val after = userService.updateNickname(userId = before.id, nickname= nickname)
        assertAll(
            {assertThat(after).isNotEqualTo(beforeNickname)}
        )
    }
}
