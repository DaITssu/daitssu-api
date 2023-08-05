package com.example.common.enums

private const val MAIN_NUMBERING = 1000
private const val NOTICE_NUMBERING = 2000
private const val COURSE_NUMBERING = 3000
private const val SERVER_NUMBERING = 4000

enum class ErrorCode(val code: Int, val message: String) {
    USER_NOT_FOUND(MAIN_NUMBERING + 1, "유저를 찾을 수 없습니다"),
    DEPARTMENT_NOT_FOUND(MAIN_NUMBERING + 2, "학과를 찾을 수 없습니다"),
    COURSE_NOT_FOUND(MAIN_NUMBERING + 3, "과목을 찾을 수 없습니다."),
    
    BAD_REQUEST(SERVER_NUMBERING + 1, "잘못된 요청입니다."),
    INVALID_FORMAT(SERVER_NUMBERING + 2, "잘못된 형식입니다."),
}