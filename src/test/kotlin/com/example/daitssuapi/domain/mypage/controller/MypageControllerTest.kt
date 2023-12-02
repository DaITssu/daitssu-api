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
    @DisplayName("스크랩이 있는 userId를 이용하여 조회시_1개 이상의 게시글이 조회된다")
    fun get_my_scraps_with_user_id(){
        val userId = 1L
        
        mockMvc.perform((get("$url/$userId/scraps")))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.data").isNotEmpty)
    }
    
    @Test
    @DisplayName("스크랩이 없는 userId를 이용하여 조회시_ 빈 List를 받는다")
    fun get_my_scraps_with_wrong_user_id() {
        val wrongUserId = 2L
        
        mockMvc.perform((get("$url/$wrongUserId/scraps")))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.data").isEmpty)
    }
}