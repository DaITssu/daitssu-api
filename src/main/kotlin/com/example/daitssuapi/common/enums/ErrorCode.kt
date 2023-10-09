package com.example.daitssuapi.common.enums

private const val MAIN_NUMBERING = 1000
private const val NOTICE_NUMBERING = 2000
private const val COURSE_NUMBERING = 3000
private const val SERVER_NUMBERING = 4000
private const val INFRA_NUMBERING = 5000

enum class ErrorCode(val code: Int, val message: String) {
    USER_NOT_FOUND(MAIN_NUMBERING + 1, "유저를 찾을 수 없습니다"),
    DEPARTMENT_NOT_FOUND(MAIN_NUMBERING + 2, "학과를 찾을 수 없습니다"),
    ARTICLE_NOT_FOUND(MAIN_NUMBERING + 3, "게시글을 찾을 수 없습니다."),
    NICKNAME_REQUIRED(MAIN_NUMBERING + 4, "닉네임이 필요한 작업입니다."),
    USER_NICKNAME_MISSING(MAIN_NUMBERING + 5, "유저의 닉네임이 없습니다"),
    COMMENT_NOT_FOUND(MAIN_NUMBERING + 6, "원 댓글이 없습니다"),
    COMMENT_TOO_LONG(MAIN_NUMBERING + 7, "댓글이 너무 깁니다"),
    DIFFERENT_ARTICLE(MAIN_NUMBERING + 8, "다른 게시글에 달린 댓글의 대댓글입니다"),

    NOTICE_NOT_FOUND(NOTICE_NUMBERING + 1, "공지사항 내용을 찾을 수 없습니다"),
    FUNSYSTEM_NOT_FOUND(NOTICE_NUMBERING + 2, "펀시스템 내용을 찾을 수 없습니다"),
    INVALID_CATEGORY(NOTICE_NUMBERING + 3, "존재하지 않는 카테고리입니다"),
    DIFFERENT_NOTICE(NOTICE_NUMBERING + 4, "다른 공지사항에 달린 댓글의 대댓글입니다"),

    COURSE_NOT_FOUND(COURSE_NUMBERING + 1, "과목을 찾을 수 없습니다."),
    USER_COURSE_RELATION_NOT_FOUND(COURSE_NUMBERING + 2, "유저가 수강중인 강의를 찾을 수 없습니다."),
    CALENDAR_NOT_FOUND(COURSE_NUMBERING + 3, "캘린더를 찾을 수 없습니다."),

    BAD_REQUEST(SERVER_NUMBERING + 1, "잘못된 요청입니다."),
    INVALID_FORMAT(SERVER_NUMBERING + 2, "잘못된 형식입니다."),
    INVALID_DATE_FORMAT(SERVER_NUMBERING + 3, "잘못된 날짜 형식입니다. yyyy-MM-dd HH:mm:ss 형식으로 요청바랍니다."),
    INVALID_GET_DATE_FORMAT(SERVER_NUMBERING + 4, "잘못된 날짜 형식입니다. yyyy-MM 형식으로 요청바랍니다."),

    S3_UPLOAD_FAILED(INFRA_NUMBERING + 1, "S3 객체 생성에 실패했습니다."),
}
