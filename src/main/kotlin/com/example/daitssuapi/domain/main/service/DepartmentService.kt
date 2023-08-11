package com.example.daitssuapi.domain.main.service

import com.example.daitssuapi.domain.main.dto.response.DepartmentResponse
import com.example.daitssuapi.domain.main.model.entity.Department
import com.example.daitssuapi.domain.main.model.repository.DepartmentRepository
import com.example.daitssuapi.enums.ErrorCode
import com.example.daitssuapi.exception.DefaultException
import org.springframework.stereotype.Service

@Service
class DepartmentService(
    private val departmentRepository: DepartmentRepository
) {
    fun getDepartment(name: String): DepartmentResponse {
        val department = departmentRepository.findByName(name)
            ?: throw DefaultException(errorCode = ErrorCode.DEPARTMENT_NOT_FOUND)

        return DepartmentResponse(id = department.id, name = department.name)
    }

    fun makeDepartment(name: String): DepartmentResponse {
        val department = Department(name)

        return DepartmentResponse(id = departmentRepository.save(department).id, name = name)
    }
}
