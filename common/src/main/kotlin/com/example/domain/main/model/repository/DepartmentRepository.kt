package com.example.domain.main.model.repository

import com.example.domain.main.model.entity.Department
import org.springframework.data.jpa.repository.JpaRepository

interface DepartmentRepository : JpaRepository<Department, Long> {
    fun findByName(name: String): Department?
}
