package com.clnine.kimpd.src.Web.user.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostLoginRes {
    private int userIdx;
    private String jwt;
    private String nickname;
    private String profileImageURL;
    private String userType;
    private String jobCategoryChildName;
    @Builder
    public PostLoginRes(int userIdx,String jwt, String nickname, String profileImageURL, String userType, String jobCategoryChildName) {
        this.userIdx = userIdx;
        this.jwt = jwt;
        this.nickname = nickname;
        this.profileImageURL = profileImageURL;
        this.userType = userType;
        this.jobCategoryChildName = jobCategoryChildName;
    }
    @Builder
    public PostLoginRes(int userIdx,String jwt, String nickname, String profileImageURL, String userType) {
        this.userIdx = userIdx;
        this.jwt = jwt;
        this.nickname = nickname;
        this.profileImageURL = profileImageURL;
        this.userType = userType;
    }
}
