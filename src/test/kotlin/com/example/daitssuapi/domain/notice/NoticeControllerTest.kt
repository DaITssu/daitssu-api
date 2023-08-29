package com.example.daitssuapi.domain.notice

import com.example.daitssuapi.domain.notice.model.repository.NoticeRepository
import com.example.daitssuapi.domain.notice.service.NoticeService
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.jdbc.Sql


@AutoConfigureMockMvc
@MockBean(JpaMetamodelMappingContext::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class NoticeControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var noticeService: NoticeService

    @Autowired
    private lateinit var noticeRepository: NoticeRepository

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

        val result = mockMvc.get("/notice/학사")
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
    @DisplayName("Notice 페이징 확인")
    fun getNoticePage(){
        val result = mockMvc.get("/notice/page/1")
            .andExpect {
                status { isOk() }

            }
            .andReturn()
        println(result.response.contentAsString)
    }

}