package com.example.daitssuapi.domain.notice.controller

import com.example.daitssuapi.common.dto.Response
import com.example.daitssuapi.common.enums.ErrorCode
import com.example.daitssuapi.common.enums.FunSystemCategory
import com.example.daitssuapi.common.exception.DefaultException
import com.example.daitssuapi.domain.notice.dto.FunSystemPageResponse
import com.example.daitssuapi.domain.notice.dto.FunSystemResponse
import com.example.daitssuapi.domain.notice.service.FunSystemService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/funsystem")
class FunSystemController(
    private val funSystemService: FunSystemService
){

    @GetMapping
    fun getAllFunSystemList(
        @RequestParam searchKeyword:String? = null
    ):Response<List<FunSystemResponse>>{
        return Response(data = funSystemService.getAllFunSystemList(searchKeyword))
    }

    @GetMapping("/{category}")
    fun getFunSystemListWithCategory(
        @PathVariable category:FunSystemCategory,
        @RequestParam searchKeyword:String? = null,
    ): Response<List<FunSystemResponse>>{

        return Response(data = funSystemService.getFunSystemList(category, searchKeyword))
    }


    @GetMapping("/page/{id}")
    fun getFunSystemPage(
        @PathVariable id : Long,
    ):Response<FunSystemPageResponse>{
        funSystemService.updateViews(id)
        return Response(data = funSystemService.getFunSystemPage(id))
    }

}