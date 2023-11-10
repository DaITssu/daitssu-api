package com.example.daitssuapi.domain.notice

import com.example.daitssuapi.utils.ControllerTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath

@ControllerTest
class FunSystemControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    @DisplayName("FunSystem 리스트 컨트롤러 로직 확인")
    fun getAllFunSystem() {

        // Act and Assert
        val result = mockMvc.get("/funsystem/ALL")
            .andExpect {
                status { isOk() }

            }
            .andReturn()
        println(result.response.contentAsString)
    }

    @Test
    @DisplayName("Funsystem 리스트 카테고리별 검색 확인")
    fun getSomeFunSystemList() {

        mockMvc.get("/funsystem/SUBSCRIPTION")
            .andExpect {
                status { isOk() }
                content {
                    jsonPath("$.data[0].id").value(2)
                    jsonPath("$.data[0].title").value("공지사항2")
                    jsonPath("$.data[0].content").value("2번 공지 내용입니다!!")
                    jsonPath("$.data[0].category").value("SUBSCRIPTION")
                    jsonPath("$.data[0].imageUrl").doesNotExist()
                    jsonPath("$.data[0].url").doesNotExist()
                    jsonPath("$.data[0].createdAt").value("0999-12-27T00:32:08")
                    jsonPath("$.data[0].updatedAt").value("0999-12-27T00:32:08")
                }
            }
    }

    @Test
    @DisplayName("Funsystem 페이징 확인")
    fun getFunSystemPage() {
        mockMvc.get("/funsystem/page/1")
            .andExpect {
                status { isOk() }
                content {
                    jsonPath("$.data[0].id").value(1)
                    jsonPath("$.data[0].title").value("공지사항1")
                    jsonPath("$.data[0].content").value("1번 공지 내용입니다!!")
                    jsonPath("$.data[0].category").value("CERTIFICATION")
                    jsonPath("$.data[0].imageUrl").doesNotExist()
                    jsonPath("$.data[0].url").doesNotExist()
                    jsonPath("$.data[0].createdAt").value("0999-12-27T00:32:08")
                    jsonPath("$.data[0].updatedAt").value("0999-12-27T00:32:08")
                }
            }
    }
}
