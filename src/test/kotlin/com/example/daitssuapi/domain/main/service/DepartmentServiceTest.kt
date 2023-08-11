package com.example.daitssuapi.domain.main.service

import com.example.daitssuapi.domain.main.model.entity.Department
import com.example.daitssuapi.domain.main.model.repository.DepartmentRepository
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class DepartmentServiceTest(
    private val departmentService: DepartmentService,
    private val departmentRepository: DepartmentRepository
) {
    @Test
    @DisplayName("이름을 통해 Department 조회")
    fun getDepartment() {
        val department = departmentRepository.save(Department(name = "temp"))

        val departmentResponse = departmentService.getDepartment(department.name)

        assertAll(
            { assertThat(departmentResponse.id).isEqualTo(department.id) },
            { assertThat(departmentResponse.name).isEqualTo(department.name) }
        )
    }

    @Test
    @DisplayName("이름을 통해 Department 저장")
    fun makeDepartment() {
        val name = "temp"

        val departmentResponse = departmentService.makeDepartment(name)

        assertAll(
            { assertThat(departmentResponse.id).isNotZero() },
            { assertThat(departmentResponse.name).isEqualTo(name) }
        )
    }
}
