package com.example.daitssuapi.domain.notice.service

import com.example.daitssuapi.common.enums.ErrorCode
import com.example.daitssuapi.common.enums.FunSystemCategory
import com.example.daitssuapi.common.exception.DefaultException
import com.example.daitssuapi.domain.notice.dto.FunSystemPageResponse
import com.example.daitssuapi.domain.notice.dto.FunSystemResponse
import com.example.daitssuapi.domain.notice.model.entity.FunSystem
import com.example.daitssuapi.domain.notice.model.repository.FunSystemRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class FunSystemService (
    private val funSystemRepository: FunSystemRepository
){
    fun getAllFunSystemList(searchKeyword:String?):List<FunSystemResponse>{
        val funSystems: List<FunSystem>
        if(searchKeyword==null){
            funSystems = funSystemRepository.findAll()
        }else{
            funSystems= funSystemRepository.findByTitleContaining(searchKeyword)
        }
        return funSystems.map { FunSystemResponse.fromFunSystem(it) }
    }
    fun getFunSystemList(
        category: FunSystemCategory,
        searchKeyword: String?,
    ):List<FunSystemResponse>{
        val funSystems : List<FunSystem>
        if(searchKeyword==null){
            funSystems = funSystemRepository.findByCategory(category)
        }else{
            funSystems = funSystemRepository.findByCategoryAndTitleContaining(category,searchKeyword)
        }
        return funSystems.map{FunSystemResponse.fromFunSystem(it)}
    }

    fun getFunSystemPage(
        id : Long
    ): FunSystemPageResponse {
        val funSystem : FunSystem = funSystemRepository.findByIdOrNull(id)
            ?: throw DefaultException(ErrorCode.FUNSYSTEM_NOT_FOUND)

        return FunSystemPageResponse.fromFunSystem(funSystem)
    }
    fun updateViews( id:Long ) { funSystemRepository.updateViewsById(id)}
}