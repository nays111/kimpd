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
    SUCCESS_SEND_MESSAGE(true,1024,"성공적으로 메시지를 전송하였습니다."),
    SUCCESS_SEND_ID_MESSAGE(true,1025,"성공적으로 ID를 메시지로 전송하였습니다."),
    SUCCESS_PATCH_USER_PASSWORD(true,1026,"입력하신 메일로 새 비밀번호를 발급하였습니다."),
    SUCCESS_GET_HOME_RES(true,1027,"홈화면 조회에 성공했습니다."),
    SUCCESS_GET_PARENT_JOB_CATEGORY(true,1028,"1차 직종 카테고리 조회에 성공하였습니다."),
    SUCCESS_GET_GENRE_CATEGORY(true,1029,"장르 카테고리 조회에 성공하였습니다."),
    SUCCESS_GET_CHILD_JOB_CATEGORY(true,1030,"2차 직종 카테고리 조회에 성공하였습니다."),
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
    EMPTY_BUSINESS_NUMBER(false,2058,"사업자 등록번호를 입력해주세요."),
    EMPTY_BUSINESS_IMAGE(false,2059,"사업자 등록증을 업로드해주세요."),
    EMPTY_BUSINESS_NAME(false,2060,"개인 사업자명을 입력해주세요"),
    EMPTY_CORP_BUSINESS_NUMBER(false,2061,"법인 사업자 등록번호를 입력해주세요."),
    NO_SELECT_GENRE_CATEGORY(false,2062,"장르 카테고리를 선택해주세요."),
    EMPTY_CORP_BUSINESS_NAME(false,2063,"법인 사업자명을 입력해주세요."),
    EMPTY_JOB_CATEGORY_PARENT_IDX(false,2064,"1차 직종 카테고리 번호를 먼저 입력해주세요."),
    NO_SELECT_JOB_PARENT_CATEGORY(false,2065,"1차 직종 카테고리를 선택해주세요."),
    NO_SELECT_JOB_CHILD_CATEGORY(false,2066,"2차 직종 카테고리를 선택해주세요."),
    EMPTY_PRIVATE_BUSINESS_NAME(false,2067,"개인 사업자명을 입력해주세요."),
    EMPTY_CITY(false,2068,"도시명을 입력해주세요."),
    INVALID_USER_TYPE(false,2069,"잘못된 유저 타입입니다."),
    INVALID_ID(false,2070,"잘못된 ID 형식입니다."),
    INVALID_IMAGE_TYPE(false,2071,"잘못된 파일 형식입니다."),
    INVALID_NICKNAME(false,2072,"잘못된 닉네임 형식입니다.(2~7자리로 입력해주세요.)"),
    EMPTY_ADDRESS(false,2073, "주소를 입력해주세요."),
    EMPTY_CODE(false,2074,"인증번호를 입력해주세요."),
    WRONG_SECURE_CODE(false,2075,"인증번호가 틀렷습니다."),
    EMPTY_PROJECT_MANAGER(false,2076,"프로젝트 담당자를 입력해주세요."),
    EMPTY_REVIEW_DESCRIPTION(false,2077,"후기를 입력해주세요"),
    WRONG_REVIEW_STAR(false,2078,"올바른 별점을 입력해주세요."),
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
    FAILED_TO_SEND_MESSAGE(false,3023,"메시지 전송에 실패하였습니다."),
    FAILED_TO_POST_SECURE_CODE(false,3024,"인증번호를 저장 못했습니다."),
    FAILED_TO_POST_USER_GENRE_CATEGORY(false,3025,"장르 카테고리를 저장하지 못했습니다"),
    FAILED_TO_POST_USER_JOB_CATEGORY(false,3026,"직종 카테고리를 저장하지 못했습니다."),
    FAILED_TO_GET_PARENT_JOB_CATEGORIES(false,3027, "직종 카테고리 리스트 조회에 실패했습니다."),
    FAILED_TO_GET_BANNERS(false,3028,"배너 리스트 조회에 실패했습니다."),
    FAILED_TO_GET_CHILD_JOB_CATEGORIES(false,3029,"2차 직종 카테고리 리스트 조회에 실패했습니다."),
    FAILED_TO_GET_GENRE_CATEGORIES(false,3030,"장르 카테고리 조회에 실패했습니다."),
    FAILED_TO_GET_PROJECTS_LIST(false,3031,"프로젝트 리스트 조회에 실패하였습니다."),
    FAILED_TO_GET_SECURE_CODE(false,3032, "인증번호 조회에 실패했습니다."),
    FAILED_TO_RECASTING(false,3033,"재섭외 요청에 실패했습니다."),
    FAILED_TO_POST_REVIEW(false,3034,"평가에 실패했습니다."),
    FAILED_TO_GET_CONTRACT(false,3035,"계약서를 불러오는데 실패했습니다"),
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
