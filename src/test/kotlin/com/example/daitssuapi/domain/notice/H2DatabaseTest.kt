package com.example.daitssuapi.domain.notice

import com.example.daitssuapi.domain.notice.dto.NoticeResponse
import com.example.daitssuapi.domain.notice.service.NoticeService
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql

@SpringBootTest
class H2DatabaseTest{
    @Autowired
    private lateinit var noticeService : NoticeService


    @Sql("classpath:schema.sql")
    @Sql("classpath:data.sql")
    @DisplayName("Notice 데이터베이스 테스트 ")
    @Test
    fun init(){
        val list : List<NoticeResponse> = noticeService.getNoticeList("전체")
        System.out.println("start!")
        list.forEach(System.out::println)
        System.out.println("end!")

    }

    @Test
    fun realTest(){
        System.out.println("hello test!")
    }
}