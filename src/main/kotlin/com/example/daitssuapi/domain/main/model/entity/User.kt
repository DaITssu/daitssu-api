package com.example.daitssuapi.domain.main.model.entity

import com.example.daitssuapi.common.audit.BaseEntity
import jakarta.persistence.*

@Entity
@Table(schema = "main", name = "users")
class User(
    // TODO: int to string 변경
    val studentId: String,

    val name: String,

    val nickname: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    val department: Department,

    var imageUrl: String? = null,

    var term: Int,

    // TODO: password bytearray 칼럼 추가하기
    var password: String,

    // TODO: 칼럼 추가
    var ssuToken: String,

    var refreshToken: String,
) : BaseEntity()
