package com.clnine.kimpd.src.Web.user.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class GetMyUserInfoRes {
    private final int userIdx;
    private final String profileImageURL;
    private final String id;
    private final String nickname;
    private final String phoneNum;
    private final String email;
    /**
     * 개인 사업자용
     */
    private final String privateBusinessName;
    private final String businessNumber;
    private final String businessImageURL;

    /**
     * 법인 사업자용
     */
    private final String corpBusinessName;
    private final String corpBusinessNumber;



}
