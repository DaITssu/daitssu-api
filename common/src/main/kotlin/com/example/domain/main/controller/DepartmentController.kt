package com.example.domain.main.controller

import com.example.common.dto.Response
import com.example.domain.main.dto.request.DepartmentRequest
import com.example.domain.main.dto.response.DepartmentResponse
import com.example.domain.main.service.DepartmentService
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/daitssu/department")
class DepartmentController(
    private val departmentService: DepartmentService
) {
    @GetMapping("/{name}")
    fun getDepartment(
        @PathVariable name: String
    ): Response<DepartmentResponse> =
        Response(data = departmentService.getDepartment(name = name))

    @PostMapping
    fun makeDepartment(
        @RequestBody @Validated departmentRequest: DepartmentRequest
    ): Response<DepartmentResponse> =
        Response(data = departmentService.makeDepartment(name = departmentRequest.name))
}
