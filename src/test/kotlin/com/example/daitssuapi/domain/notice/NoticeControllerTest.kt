package com.example.daitssuapi.domain.notice

import com.example.daitssuapi.utils.ControllerTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath


@ControllerTest
class NoticeControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    @DisplayName("Notice 리스트 컨트롤러 로직 확인")
    fun getAllNoticeList() {

        // Act and Assert
        val result = mockMvc.get("/notice/ALL")
            .andExpect {
                status { isOk() }

            }
            .andReturn()
        println(result.response.contentAsString)
    }

    @Test
    @DisplayName("Notice 리스트 카테고리별 검색 확인")
    fun getSomeNoticeList() {

        mockMvc.get("/notice/ACADEMICS")
            .andExpect {
                status { isOk() }
                content {
                    jsonPath("$.data[0].id").value(2)
                    jsonPath("$.data[0].title").value("공지사항2")
                    jsonPath("$.data[0].departmentId").value(2)
                    jsonPath("$.data[0].content").value("2번 공지 내용입니다!!")
                    jsonPath("$.data[0].category").value("SUBSCRIPTION")
                    jsonPath("$.data[0].imageUrl").doesNotExist()
                    jsonPath("$.data[0].fileUrl").doesNotExist()
                    jsonPath("$.data[0].createdAt").value("0999-12-27T00:32:08")
                    jsonPath("$.data[0].updatedAt").value("0999-12-27T00:32:08")
                }
            }
    }

    @Test
    @DisplayName("Notice 페이징 확인")
    fun getNoticePage() {
        mockMvc.get("/notice/page/1")
            .andExpect {
                status { isOk() }
                content {
                    jsonPath("$.data[0].id").value(1)
                    jsonPath("$.data[0].title").value("공지사항1")
                    jsonPath("$.data[0].departmentId").value(1)
                    jsonPath("$.data[0].content").value("1번 공지 내용입니다!!")
                    jsonPath("$.data[0].category").value("ACADEMICS")
                    jsonPath("$.data[0].imageUrl").doesNotExist()
                    jsonPath("$.data[0].fileUrl").doesNotExist()
                    jsonPath("$.data[0].createdAt").value("0999-12-27T00:32:08")
                    jsonPath("$.data[0].updatedAt").value("0999-12-27T00:32:08")
                }
            }
    }
}
