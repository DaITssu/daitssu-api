package com.example.daitssuapi.domain.mypage.controller

import com.example.daitssuapi.utils.ControllerTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@ControllerTest
class MypageControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc
    
    private val url = "/mypage"
    
    @Test
    @DisplayName("게시글이 있는 userId를 이용하여 게시글 조회시 _ 1개 이상의 게시글이 조회된다")
    fun get_my_articles_with_user_id() {
        val userId = 4L
        
        mockMvc.perform(get("$url/$userId/articles"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.data").isNotEmpty)
     
    }
    
    @Test
    @DisplayName("게시글이 없는 userId를 이용하여 게시글 조회시 _ 빈 List를 받는다")
    fun get_my_articles_with_wrong_user_id() {
        val wrongUserID = 1L
        
        mockMvc.perform(get("$url/$wrongUserID/articles"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.data").isEmpty)
    }

    @Test
    @DisplayName("서비스 공지사항 전제 조회")
    fun get_service_notice() {

        mockMvc.perform(get("$url/service-notice"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.data").isNotEmpty)
            .andReturn()
    }
    
}