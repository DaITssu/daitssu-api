package com.example.daitssuapi.domain.user.model.repository

import com.example.daitssuapi.domain.user.model.entity.Department
import org.springframework.data.jpa.repository.JpaRepository

interface DepartmentRepository : JpaRepository<Department, Long> {
    fun findByName(name: String): Department?
}
