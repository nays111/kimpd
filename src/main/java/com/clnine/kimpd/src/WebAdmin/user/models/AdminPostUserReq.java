package com.clnine.kimpd.src.WebAdmin.user.models;

import com.sun.istack.Nullable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PUBLIC) // Unit Test 를 위해 PUBLIC
@Getter
public class AdminPostUserReq {
    private int userType;
    private String id;
    private String email;
    private String phoneNum;
    private String name;
    @Nullable
    private String city;
    @Nullable
    private String nickname;
    @Nullable
    private String profileImageURL;
    @Nullable
    private String introduce;
    @Nullable
    private String career;
    @Nullable
    private String etc;
    private int minimumCastingPrice;
    @Nullable
    private String privateBusinessName;
    @Nullable
    private String businessNumber;
    @Nullable
    private String businessImageURL;
    @Nullable
    private String corporationBusinessName;
    @Nullable
    private String castingPossibleStartDate;
    @Nullable
    private String castingPossibleEndDate;
}
