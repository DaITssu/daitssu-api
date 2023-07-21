package com.example.domain.main.service

import com.example.common.enums.ErrorCode
import com.example.common.exception.DefaultException
import com.example.domain.main.dto.response.DepartmentResponse
import com.example.domain.main.model.entity.Department
import com.example.domain.main.model.repository.DepartmentRepository
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
