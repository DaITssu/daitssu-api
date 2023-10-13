package com.example.daitssuapi.domain.notice

import com.example.daitssuapi.domain.notice.model.repository.NoticeRepository
import com.example.daitssuapi.domain.notice.service.NoticeService
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
import org.springframework.test.web.servlet.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
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

        val result = mockMvc.get("/notice")
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
    @DisplayName("Notice 리스트 전체 검색 확인")
    fun getAllNoticeListWithMark() {

        val expected = noticeService.getNoticePage(4)
        val result = mockMvc.get("/notice?searchKeyword=4")
            .andExpect {
                status { isOk() }
                content{
                    jsonPath("$.data[0].id").value(expected.id)
                }
            }


    }
    @Sql("classpath:schema.sql")
    @Sql("classpath:data.sql")
    @Test
    @WithMockUser
    @DisplayName("Notice 리스트 카테고리 검색 확인")
    fun getSomeNoticeList() {

        mockMvc.get("/notice/SUBSCRIPTION")
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
    @Sql("classpath:data.sql")
    @Test
    @WithMockUser
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
    @Sql("classpath:schema.sql")
    @Sql("classpath:data.sql")
    @Test
    @WithMockUser
    @DisplayName("Notice 리스트 카테고리별 검색 확인") // 같은 카테고리로 4번과 5번이 있는데 둘중 5 하나만 검색해서 나오게 함
    fun getSearchedNoticeList() {

        mockMvc.get("/notice/INTERNATIONAL_EXCHANGE?searchKeyword=5")
            .andExpect {
                status { isOk() }
                content {
                    jsonPath("$.data[0].id").value(5)
                    jsonPath("$.data[0].title").value("공지사항5!")
                    jsonPath("$.data[0].departmentId").value(5)
                    jsonPath("$.data[0].content").value("5번 공지 내용입니다!!")
                    jsonPath("$.data[0].category").value("INTERNATIONAL_EXCHANGE")
                    jsonPath("$.data[0].imageUrl").doesNotExist()
                    jsonPath("$.data[0].fileUrl").doesNotExist()
                    jsonPath("$.data[0].createdAt").value("0999-12-27T00:32:08")
                    jsonPath("$.data[0].updatedAt").value("0999-12-27T00:32:08")
                }
            }

    }
    @Sql("classpath:schema.sql")
    @Sql("classpath:data.sql")
    @Test
    @WithMockUser
    @DisplayName("Notice view 테스팅")
    fun getNoticePageViewsTest() {
        val result = mockMvc.get("/notice/page/1")
            .andExpect {
                status { isOk() }

            }.andReturn()

        println(result.response.contentAsString)
    }
}