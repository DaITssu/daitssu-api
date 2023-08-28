package com.example.daitssuapi.domain.notice.model.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

@Entity
@Table(schema = "notice")
class FunSystem (

    @Id
    @Column(name = "id")
    val id: Long? =null,

    @Column(name = "title", length= 1024, nullable = false)
    val title:String,

    @Column(name = "content", nullable = false)
    val content : String,

    @Column(name = "category", nullable= false)
    val category:String,

    @Column(name = "image_url")
    val imageUrl :String,

    @Column(name = "url", nullable = false)
    val url : String,

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    val createdAt : LocalDateTime,

    @UpdateTimestamp
    @Column(name = "updated_at" , nullable= false)
    val updatedAt : LocalDateTime,

    // view 빠짐
)