package com.example.daitssuapi.common.enums

private const val MAIN_NUMBERING = 1000
private const val NOTICE_NUMBERING = 2000
private const val COURSE_NUMBERING = 3000
private const val SERVER_NUMBERING = 4000

enum class ErrorCode(val code: Int, val message: String) {
    USER_NOT_FOUND(MAIN_NUMBERING + 1, "유저를 찾을 수 없습니다"),
    DEPARTMENT_NOT_FOUND(MAIN_NUMBERING + 2, "학과를 찾을 수 없습니다"),
    ARTICLE_NOT_FOUND(MAIN_NUMBERING + 3, "게시글을 찾을 수 없습니다."),
    NICKNAME_REQUIRED(MAIN_NUMBERING + 4, "닉네임이 필요한 작업입니다."),
    USER_NICKNAME_MISSING(MAIN_NUMBERING + 5, "유저의 닉네임이 없습니다"),
    PASSWORD_INCORRECT(MAIN_NUMBERING + 6, "패스워드가 일치하지 않습니다."),
    USER_ALREADY_EXISTS(MAIN_NUMBERING + 7, "이미 존재하는 유저입니다."),
    REFRESH_TOKEN_NOT_FOUND(MAIN_NUMBERING + 8, "토큰을 리프레시 할 수 없습니다."),
    TOKEN_INVALID(MAIN_NUMBERING + 9, "유효하지 않은 토큰입니다."),

    COURSE_NOT_FOUND(COURSE_NUMBERING + 1, "과목을 찾을 수 없습니다."),
    USER_COURSE_RELATION_NOT_FOUND(COURSE_NUMBERING + 2, "유저가 수강중인 강의를 찾을 수 없습니다."),

    BAD_REQUEST(SERVER_NUMBERING + 1, "잘못된 요청입니다."),
    INVALID_FORMAT(SERVER_NUMBERING + 2, "잘못된 형식입니다."),
    INVALID_DATE_FORMAT(SERVER_NUMBERING + 3, "잘못된 날짜 형식입니다. yyyy-MM-dd HH:mm:ss 형식으로 요청바랍니다."),
}
