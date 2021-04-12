package com.clnine.kimpd.src.Web.user.models;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetUserRes {
    private int userIdx;
    private String nickname;
    private String profileImageURL;
    private int userType;
    private String jobCategoryChildName;

    @Builder
    public GetUserRes(int userIdx, String nickname, String profileImageURL, int userType, String jobCategoryChildName) {
        this.userIdx = userIdx;
        this.nickname = nickname;
        this.profileImageURL = profileImageURL;
        this.userType = userType;
        this.jobCategoryChildName = jobCategoryChildName;
    }

    @Builder
    public GetUserRes(int userIdx, String nickname, String profileImageURL, int userType) {
        this.userIdx = userIdx;
        this.nickname = nickname;
        this.profileImageURL = profileImageURL;
        this.userType = userType;
    }

}