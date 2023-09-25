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
    fun getFunSystemList(
        category:String
    ):List<FunSystemResponse>{
        val funSystems : List<FunSystem>

        if(category =="ALL")
            funSystems = funSystemRepository.findAll()
        else{
            val funSystemCategory = FunSystemCategory.fromCode(category)
            if(funSystemCategory !=null){
                funSystems = funSystemRepository.findByCategory(funSystemCategory)
            }else{
                throw DefaultException(errorCode = ErrorCode.INVALID_CATEGORY)
            }
        }
        return funSystems.map{FunSystemResponse.fromFunSystem(it)}
    }
    fun getFunSystemSearchList(
        category: String,
        searchKeyword : String
    ):List<FunSystemResponse> {

        val funSystems:List<FunSystem>

        if(category == "ALL")
            funSystems= funSystemRepository.findByTitleContaining(searchKeyword)
        else{
            funSystems = FunSystemCategory.fromCode(category)?.let {
                funSystemRepository.findByCategoryAndTitleContaining(it,searchKeyword)
            } ?: throw DefaultException(errorCode = ErrorCode.INVALID_CATEGORY)
        }


        return funSystems.map { FunSystemResponse.fromFunSystem(it) }

    }
    fun getFunSystemPage(
        id : Long
    ): FunSystemPageResponse {
        val funSystem : FunSystem = funSystemRepository.findByIdOrNull(id)
            ?: throw DefaultException(errorCode = ErrorCode.FUNSYSTEM_NOT_FOUND)

        return FunSystemPageResponse.fromFunSystem(funSystem)
    }
    fun updateViews( id:Long ) { funSystemRepository.updateViewsById(id)}
}