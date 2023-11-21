package com.example.daitssuapi.domain.notice

import com.example.daitssuapi.utils.ControllerTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath

@ControllerTest
class FunSystemControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    @WithMockUser
    @DisplayName("FunSystem 리스트 전부 가져오기")

    fun getAllFunSystem() {

        // Act and Assert
        val result = mockMvc.get("/funsystem")
            .andExpect {
                status { isOk() }

            }
            .andReturn()
        println(result.response.contentAsString)
    }

    @Test

    @WithMockUser
    @DisplayName("Funsystem 리스트 카테고리 확인")
    fun getSomeFunSystemList() {

        mockMvc.get("/funsystem/SUBSCRIPTION")
            .andExpect {
                status { isOk() }
                content {
                    jsonPath("$.data[0].id").value(2)
                    jsonPath("$.data[0].title").value("공지사항2")
                    jsonPath("$.data[0].category").value("SUBSCRIPTION")
                    jsonPath("$.data[0].createdAt").value("0999-12-27T00:32:08")
                }
            }
    }
    @Sql("classpath:schema.sql")
    @Sql("classpath:h2-data.sql")
    @Test
    @WithMockUser
    @DisplayName("FunSystem 리스트 카테고리별 검색 확인") // 같은 카테고리로 4번과 5번이 있는데 둘중 5 하나만 검색해서 나오게 함
    fun getSearchedFunSystemList() {

        val result = mockMvc.get("/funsystem/EXPERIENTIAL_ACTIVITIES?searchKeyword=5")
            .andExpect {
                status { isOk() }
                content{
                    jsonPath("$.data[0].id").value(5)
                }
            }
    }
    @Sql("classpath:schema.sql")
    @Sql("classpath:h2-data.sql")
    @Test
    @WithMockUser
    @DisplayName("실패 : FunSystem 잘못된 카테고리 테스트")
    fun getSearchedAllFunSystemList() {

        val result = mockMvc.get("/funsystem/INVALID")
            .andExpect {
                status{is5xxServerError()}
            }.andReturn()
        println(result.response.contentAsString)
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

    @Sql("classpath:schema.sql")
    @Sql("classpath:h2-data.sql")
    @Test
    @WithMockUser
    @DisplayName("FunSyetem view 증가 테스팅")
    fun getFunSystemPageViewsTest() {
        val result = mockMvc.get("/funsystem/page/1")
            .andExpect {
                status { isOk() }
                content{
                    jsonPath("$.data[0].views").value(1)
                }
            }
    }

}

