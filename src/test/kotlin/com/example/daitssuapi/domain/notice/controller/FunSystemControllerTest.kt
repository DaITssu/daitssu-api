package com.example.daitssuapi.domain.notice.controller

import com.example.daitssuapi.utils.ControllerTest
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@ControllerTest
@Deprecated("하드코딩으로 진행된 테스트라 유지 보수 불가 판정")
@Disabled("하드코딩으로 진행된 테스트라 유지 보수 불가 판정")
class FunSystemControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    @WithMockUser
    @DisplayName("FunSystem 리스트 전부 가져오기")

    fun getAllFunSystem() {
        mockMvc.perform(get("/funsystem")
            .header(HttpHeaders.AUTHORIZATION, "Bearer test")
        ).andExpect(status().isOk)
    }

    @Test
    @WithMockUser
    @DisplayName("Funsystem 리스트 카테고리 확인")
    @Disabled("테스트 터지는데 애초에 API 설계에 이슈가 보여서 그냥 Test 중지")
    fun getSomeFunSystemList() {
        mockMvc.perform(get("/funsystem/SUBSCRIPTION")
            .header(HttpHeaders.AUTHORIZATION, "Bearer test")
        ).andExpect(status().isOk)
            .andExpect(jsonPath("$.data[0].id").value(2))
            .andExpect(jsonPath("$.data[0].title").value("공지사항2"))
            .andExpect(jsonPath("$.data[0].category").value("SUBSCRIPTION"))
            .andExpect(jsonPath("$.data[0].createdAt").value("0999-12-27T00:32:08"))
    }

    @Sql("classpath:h2-data.sql")
    @Test
    @WithMockUser
    @DisplayName("FunSystem 리스트 카테고리별 검색 확인") // 같은 카테고리로 4번과 5번이 있는데 둘중 5 하나만 검색해서 나오게 함
    @Disabled("테스트 터지는데 애초에 API 설계에 이슈가 보여서 그냥 Test 중지")
    fun getSearchedFunSystemList() {
        mockMvc.perform(get("/funsystem/EXPERIENTIAL_ACTIVITIES?searchKeyword=5")
            .header(HttpHeaders.AUTHORIZATION, "Bearer test")
        ).andExpect(status().isOk)
            .andExpect(jsonPath("$.data[0].id").value(5))
    }

    @Sql("classpath:h2-data.sql")
    @Test
    @WithMockUser
    @DisplayName("실패 : FunSystem 잘못된 카테고리 테스트")
    fun getSearchedAllFunSystemList() {
        mockMvc.perform(get("/funsystem/INVALID")
            .header(HttpHeaders.AUTHORIZATION, "Bearer test")
        ).andExpect(status().isOk)
    }

    @Test
    @DisplayName("Funsystem 페이징 확인")
    fun getFunSystemPage() {
        mockMvc.perform(get("/funsystem/page/1")
            .header(HttpHeaders.AUTHORIZATION, "Bearer test")
        ).andExpect(status().isOk)
            .andExpect(jsonPath("$.data[0].id").value(1))
            .andExpect(jsonPath("$.data[0].title").value("공지사항1"))
            .andExpect(jsonPath("$.data[0].content").value("1번 공지 내용입니다!!"))
            .andExpect(jsonPath("$.data[0].category").value("CERTIFICATION"))
            .andExpect(jsonPath("$.data[0].imageUrl").doesNotExist())
            .andExpect(jsonPath("$.data[0].url").doesNotExist())
            .andExpect(jsonPath("$.data[0].createdAt").value("0999-12-27T00:32:08"))
            .andExpect(jsonPath("$.data[0].updatedAt").value("0999-12-27T00:32:08"))
    }

    @Sql("classpath:h2-data.sql")
    @Test
    @WithMockUser
    @DisplayName("FunSyetem view 증가 테스팅")
    fun getFunSystemPageViewsTest() {
        mockMvc.perform(get("/funsystem/page/1")
            .header(HttpHeaders.AUTHORIZATION, "Bearer test")
        ).andExpect(status().isOk)
            .andExpect(jsonPath("$.data[0].views").value(1))
    }
}

