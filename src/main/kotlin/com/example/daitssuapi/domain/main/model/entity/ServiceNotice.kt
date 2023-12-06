package com.example.daitssuapi.domain.main.model.entity

import com.example.daitssuapi.common.audit.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity

@Entity
class ServiceNotice(

    @Column(length = 1024)
    val title : String,

    @Column(length = 2048)
    val content : String,

) : BaseEntity()
