package com.example.daitssuapi.domain.notice.model.entity

import com.example.daitssuapi.common.audit.BaseEntity
import com.example.daitssuapi.common.enums.NoticeCategory
import jakarta.persistence.*



@Entity
@Table(schema = "notice",name= "notice")
class Notice (

    @Column(name = "title", length= 1024, nullable = false)
    val title:String,

    @Column(name = "department_id", nullable = false)
    val departmentId: Int,

    @Column(name = "content", nullable = false)
    val content : String,

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    val category: NoticeCategory,

    @Column(name = "image_url")
    val imageUrl :String,

    @Column(name = "file_url")
    val fileUrl : String,

    @Column(name = "views", nullable = false)
    var views : Int,

): BaseEntity(){

}