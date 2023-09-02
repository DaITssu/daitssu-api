package com.example.daitssuapi.domain.notice

import com.example.daitssuapi.domain.notice.model.repository.NoticeRepository
import com.example.daitssuapi.domain.notice.service.NoticeService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.filter.CharacterEncodingFilter


@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class NoticeControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var noticeService: NoticeService

    @Autowired
    private lateinit var noticeRepository: NoticeRepository

    @Autowired
    private lateinit var ctx : WebApplicationContext

    @BeforeEach
    fun setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
            .addFilters<DefaultMockMvcBuilder>(CharacterEncodingFilter("UTF-8", true))
            .build()
    }

    @Sql("classpath:schema.sql")
    @Sql("classpath:data.sql")
    @Test
    @WithMockUser
    @DisplayName("Notice 리스트 컨트롤러 로직 확인")
    fun getAllNoticeList() {

        // Act and Assert
        val result = mockMvc.get("/notice/전체")
            .andExpect {
                status { isOk() }

            }
            .andReturn()
        println(result.response.contentAsString)
    }

    @Sql("classpath:schema.sql")
    @Sql("classpath:data.sql")
    @Test
    @WithMockUser
    @DisplayName("Notice 리스트 카테고리별 검색 확인")
    fun getSomeNoticeList() {

        val result = mockMvc.get("/notice/국제교류")
            .andExpect {
                status { isOk() }

            }
            .andReturn()

        assertEquals(result.response.contentAsString,
            "{\"code\":0,\"message\":\"\",\"data\":[{\"id\":4," +
                    "\"title\":\"공지사항4\"," +
                    "\"departmentId\":4," +
                    "\"content\":\"4번 공지 내용입니다!!\"," +
                    "\"category\":\"국제교류\"," +
                    "\"imageUrl\":null," +
                    "\"fileUrl\":null," +
                    "\"createdAt\":\"0999-12-27T00:32:08\"," +
                    "\"updatedAt\":\"0999-12-27T00:32:08\"}]}")
    }

    @Sql("classpath:schema.sql")
    @Sql("classpath:data.sql")
    @Test
    @WithMockUser
    @DisplayName("Notice 페이징 확인")
    fun getNoticePage(){
        val result = mockMvc.get("/notice/page/1")
            .andExpect {
                status { isOk() }

            }
            .andReturn()
        assertEquals(result.response.contentAsString,
            "{\"code\":0,\"message\":\"\",\"data\":{\"id\":1," +
                    "\"title\":\"공지사항1\"," +
                    "\"departmentId\":1," +
                    "\"content\":\"1번 공지 내용입니다!!\"," +
                    "\"category\":\"학사\"," +
                    "\"imageUrl\":null," +
                    "\"fileUrl\":null," +
                    "\"createdAt\":\"0999-12-27T00:32:08\"," +
                    "\"updatedAt\":\"0999-12-27T00:32:08\"}}" )
    }

}