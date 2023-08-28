package com.example.daitssuapi.domain.notice.model.entity

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.sql.Timestamp
import java.time.LocalDateTime


@Entity
@Table(schema = "notice")
class Notice (

    @Id
    @Column(name = "id")
    val id: Long? =null,

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

    @CreationTimestamp
    @Column(name = "created_at", insertable = true, nullable = false)
    val createdAt : LocalDateTime,

    @UpdateTimestamp
    @Column(name = "updated_at" , insertable = true, nullable= false)
    val updatedAt : LocalDateTime,

    // view 빠짐
)