package com.example.daitssuapi.domain.auth.client

import com.example.daitssuapi.domain.auth.client.request.CrawlBaseInformationRequest
import com.example.daitssuapi.domain.auth.client.request.SmartCampusSignInRequest
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient(
    name = "smartCampusSignInClient",
    url = "https://crawler.daitssu.com",
)
interface SmartCampusCrawlerClient {
    @PostMapping("/smart-campus/auth")
    fun smartCampusSignIn(
        @RequestBody
        smartCampusSignInRequest: SmartCampusSignInRequest
    ): String

    @PostMapping("/smart-campus/crawling")
    fun crawlBaseInformation(
        @RequestBody
        crawlBaseInformationRequest: CrawlBaseInformationRequest
    )
}