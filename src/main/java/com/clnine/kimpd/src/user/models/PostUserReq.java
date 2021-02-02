package com.clnine.kimpd.src.user.models;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PUBLIC) // Unit Test 를 위해 PUBLIC
@Getter
public class PostUserReq {
    private int userType;
    private String id;
    private String password;
    private String confirmPassword;
    private String email;
    private int agreeAdvertisement;
    private String phoneNum;
    //private int certifiacteCode;
    private String businessNumber; //사업자 등록번호
    private String businessImageURL; //사업자 등록증
    private String corporationBusinessName; //법인 사업자명
    private String corporationBusinessNumber;//법인 등록번호
}
