package com.example.daitssuapi.domain.notice.controller

import com.example.daitssuapi.common.dto.Response
import com.example.daitssuapi.domain.notice.dto.FunSystemPageResponse
import com.example.daitssuapi.domain.notice.dto.FunSystemResponse
import com.example.daitssuapi.domain.notice.service.FunSystemService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/funsystem")
class FunSystemController(
    private val funSystemService: FunSystemService
){
    @GetMapping("/{category}")
    fun getFunSystemList(
        @PathVariable category:String,
        @RequestParam(required = false) searchKeyword : String?
    ): Response<List<FunSystemResponse>>{
        if(searchKeyword == null)
            return Response(data = funSystemService.getFunSystemList(category))
        else
            return Response(data = funSystemService.getFunSystemSearchList(category,searchKeyword))
    }


    @GetMapping("/page/{id}")
    fun getFunSystemPage(
        @PathVariable id : Long,
    ):Response<FunSystemPageResponse>{
        funSystemService.updateViews(id)
        return Response(data = funSystemService.getFunSystemPage(id))
    }

}