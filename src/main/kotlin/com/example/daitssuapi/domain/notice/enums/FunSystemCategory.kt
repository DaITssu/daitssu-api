package com.example.daitssuapi.domain.notice.enums

enum class FunSystemCategory(val categoryCode: String){

    SUBSCRIPTION("구독"),
    LEARNING_SKILLS("학습역량"),
    COMPETITION("공모전/경진대회"),
    CERTIFICATION("자격증/특강"),
    STUDENT_ACTIVITIES("학생활동"),
    STUDY_ABROAD("해외연수/교환학생"),
    INTERNSHIP("인턴"),
    VOLUNTEERING("봉사"),
    EXPERIENTIAL_ACTIVITIES("체험활동"),
    COUNSELING("심리/상담/진단"),
    CAREER_SUPPORT("진로지원"),
    STARTUP_SUPPORT("창업지원"),
    EMPLOYMENT_SUPPORT("취업지원"),
    Unknown("해당없음"); // 왜 이게 있는지 모르겠음...


}