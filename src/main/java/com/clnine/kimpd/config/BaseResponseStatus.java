package com.clnine.kimpd.config;

import lombok.Getter;

/**
 * 에러 코드 관리
 */
@Getter
public enum BaseResponseStatus {
    // 1000 : 요청 성공
    SUCCESS(true, 1000, "요청에 성공하였습니다."),
    SUCCESS_READ_USERS(true, 1010, "회원 전체 정보 조회에 성공하였습니다."),
    SUCCESS_READ_USER(true, 1011, "회원 정보 조회에 성공하였습니다."),
    SUCCESS_POST_USER(true, 1012, "회원가입에 성공하였습니다."),
    SUCCESS_LOGIN(true, 1013, "로그인에 성공하였습니다."),
    SUCCESS_JWT(true, 1014, "JWT 검증에 성공하였습니다."),
    SUCCESS_DELETE_USER(true, 1015, "회원 탈퇴에 성공하였습니다."),
    SUCCESS_PATCH_USER(true, 1016, "회원정보 수정에 성공하였습니다."),
    SUCCESS_READ_SEARCH_USERS(true, 1017, "회원 검색 조회에 성공하였습니다."),

    //Kimpd response code
    SUCCESS_SEND_TEMP_PASSWORD(true,1018,"메일로 임시 비밀번호를 발급하였습니다."),
    SUCCESS_CHECK_ID(true,1019,"사용할 수 있는 ID입니다"),
    SUCCESS_CHECK_NICKNAME(true,1020,"사용할 수 있는 닉네임입니다"),
    SUCCESS_READ_PROJECTS(true,1021,"프로젝트 조회에 성공하였습니다."),
    SUCCESS_POST_PROJECT(true,1022,"프로젝트 생성에 성공하였습니다."),
    SUCCESS_POST_CASTING(true,1023,"섭외 요청에 성공하였습니다."),
    // 2000 : Request 오류
    REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),
    EMPTY_USERID(false, 2001, "유저 아이디 값을 확인해주세요."),
    EMPTY_JWT(false, 2010, "JWT를 입력해주세요."),
    INVALID_JWT(false, 2011, "유효하지 않은 JWT입니다."),
    EMPTY_EMAIL(false, 2020, "이메일을 입력해주세요."),
    INVALID_EMAIL(false, 2021, "이메일 형식을 확인해주세요."),
    EMPTY_PASSWORD(false, 2030, "비밀번호를 입력해주세요."),
    EMPTY_CONFIRM_PASSWORD(false, 2031, "비밀번호 확인을 입력해주세요."),
    WRONG_PASSWORD(false, 2032, "비밀번호를 다시 입력해주세요."),
    DO_NOT_MATCH_PASSWORD(false, 2033, "비밀번호와 비밀번호확인 값이 일치하지 않습니다."),
    EMPTY_NICKNAME(false, 2040, "닉네임을 입력해주세요."),
    EMPTY_ID(false, 2041, "아이디를 입력해주세요."),
    INVALID_PASSWORD(false,2042, "비밀번호 형식을 확인해주세요."),
    EMPTY_PHONE_NUMBER(false,2043,"휴대폰 번호를 입력해주세요."),
    INVALID_PHONE_NUMBER(false,2044,"휴대폰 형식을 확인해주세요."),
    EMPTY_PROJECT_NAME(false,2045,"프로젝트 이름을 입력해주세요."),
    EMPTY_PROJECT_MAKER(false,2046,"프로젝트 제작사를 입력해주세요."),
    EMPTY_PROJECT_START_DATE(false,2047,"프로젝트 시작일을 입력해주세요."),
    EMPTY_PROJECT_END_DATE(false,2048,"프로젝트 종료일을 입력해주세요."),
    EMPTY_PROJECT_DESCRIPTION(false,2049,"프로젝트 설명을 입력해주세요."),
    EMPTY_PROJECT_BUDGET(false,2050,"프로젝트 예산을 입력해주세요."),
    EMPTY_CASTING_PRICE(false,2051,"섭외 비용을 입력해주세요."),
    EMPTY_CASTING_START_DATE(false,2052,"섭외 시작일을 입력해주세요."),
    EMPTY_CASTING_END_DATE(false,2053,"섭외 종료일을 입력해주세요."),
    EMPTY_CASTING_PRICE_DATE(false,2054,"입금 기한을 입력해주세요"),
    EMPTY_CASTING_WORK(false,2055,"담당 업무를 입력해주세요."),
    EMPTY_CASTING_MESSAGE(false,2056,"섭외 메세지를 입력해주세요."),
    EMPTY_PROJECT_FILE_URL(false,2057,"프로젝트 파일을 입력해주세요."),

    // 3000 : Response 오류
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),
    NOT_FOUND_USER(false, 3010, "존재하지 않는 회원입니다."),
    DUPLICATED_USER(false, 3011, "이미 존재하는 회원입니다."),
    FAILED_TO_GET_USER(false, 3012, "회원 정보 조회에 실패하였습니다."),
    FAILED_TO_POST_USER(false, 3013, "회원가입에 실패하였습니다."),
    FAILED_TO_LOGIN(false, 3014, "로그인에 실패하였습니다."),
    FAILED_TO_DELETE_USER(false, 3015, "회원 탈퇴에 실패하였습니다."),
    FAILED_TO_PATCH_USER(false, 3016, "개인정보 수정에 실패하였습니다."),
    FAILED_TO_POST_PROJECT(false,3017,"프로젝트 생성에 실패하였습니다."),
    FAILED_TO_GET_PROJECTS(false,3018,"프로젝트를 불러오는데 실패하였습니다."),
    FAILED_TO_POST_CASTING(false,3019,"섭외 신청에 실패하였습니다."),
    ALREADY_SEND_CASTING_TO_EXPERT_WITH_THIS_PROJECT(false,3020,"이미 해당 프로젝트에 대해 해당 유저에게 섭외 요청을 보낸적이 있습니다."),
    FAILED_TO_GET_CASTING(false,3021,"캐스팅 정보 조회에 실패하였습니다."),
    NOT_FOUND_CASTING(false,3022,"존재하지 않는 캐스팅입니다."),
    // 4000 : Database 오류
    SERVER_ERROR(false, 4000, "서버와의 통신에 실패하였습니다."),
    DATABASE_ERROR(false, 4001, "데이터베이스 연결에 실패하였습니다.");

    // 5000 : 필요시 만들어서 쓰세요
    // 6000 : 필요시 만들어서 쓰세요

    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
