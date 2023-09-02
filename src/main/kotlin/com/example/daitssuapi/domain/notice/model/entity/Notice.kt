package com.example.daitssuapi.domain.notice.model.entity

import com.example.daitssuapi.common.audit.BaseEntity
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.sql.Timestamp
import java.time.LocalDateTime


@Entity
@Table(schema = "notice")
class Notice (

    @Column(name = "title", length= 1024, nullable = false)
    val title:String,

    @Column(name = "department_id", nullable = false)
    val departmentId: Int,

    @Column(name = "content", nullable = false)
    val content : String,

    @Column(name = "category", nullable= false)
    val category:String,

    @Column(name = "image_url")
    val imageUrl :String,

    @Column(name = "file_url")
    val fileUrl : String,

    // view 빠짐
): BaseEntity()