package com.example.daitssuapi.domain.notice.controller

import com.example.daitssuapi.common.dto.Response
import com.example.daitssuapi.domain.notice.dto.FunSystemPageResponse
import com.example.daitssuapi.domain.notice.dto.FunSystemResponse
import com.example.daitssuapi.domain.notice.service.FunSystemService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/funsystem")
class FunSystemController(
    private val funSystemService: FunSystemService
){
    @GetMapping("/{category}")
    fun getFunSystemList(
        @PathVariable category:String
    ): Response<List<FunSystemResponse>> =
        Response(data = funSystemService.getFunSystemList(category))

    @GetMapping("/page/{id}")
    fun getFunSystemPage(
        @PathVariable id : Long,
    ):Response<FunSystemPageResponse> =
        Response(data = funSystemService.getFunSystemPage(id))
}