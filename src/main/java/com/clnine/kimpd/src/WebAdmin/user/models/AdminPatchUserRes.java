package com.clnine.kimpd.src.WebAdmin.user.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AdminPatchUserRes {
    private final int userIdx;
    private final String userType;
    private final String id;
    private final String email;
    private final String phoneNum;
    private final String city;
    private final String nickname;
    private final String profileImageURL;
    private final String introduce;
    private final String career;
    private final String etc;
    private final String minimumCastingPrice;
    private final String privateBusinessName;
    private final String businessNumber;
    private final String businessImageURL;
    private final String corporationBusinessName;
    private final String corporationBusinessNumber;
    private final String status;
}