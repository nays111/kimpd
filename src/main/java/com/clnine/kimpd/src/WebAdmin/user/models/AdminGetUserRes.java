package com.clnine.kimpd.src.WebAdmin.user.models;

import com.sun.istack.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AdminGetUserRes {
    private final int userIdx;
    private final String userType;
    private final String id;
    private final String email;
    private final String phoneNum;
    @Nullable
    private final String city;
    @Nullable
    private final String nickname;
    @Nullable
    private final String profileImageURL;
    @Nullable
    private final String introduce;
    @Nullable
    private final String career;
    @Nullable
    private final String etc;
    @Nullable
    private final String minimumCastingPrice;
    @Nullable
    private final String privateBusinessName;
    @Nullable
    private final String businessNumber;
    @Nullable
    private final String businessImageURL;
    @Nullable
    private final String corporationBusinessName;
    @Nullable
    private final String corporationBusinessNumber;
    private final String status;
}