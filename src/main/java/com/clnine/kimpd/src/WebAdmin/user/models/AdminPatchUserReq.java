package com.clnine.kimpd.src.WebAdmin.user.models;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PUBLIC) // Unit Test 를 위해 PUBLIC
@Getter
public class AdminPatchUserReq {
    private int userIdx;
    private String userType;
    private String id;
    private String email;
    private String phoneNum;
    private String name;
    private String city;
    private String nickname;
    private String profileImageURL;
    private String introduce;
    private String career;
    private String etc;
    private String minimumCastingPrice;
    private String privateBusinessName;
    private String businessNumber;
    private String businessImageURL;
    private String corporationBusinessName;
    private String corporationBusinessNumber;
    private String status;
}