package com.clnine.kimpd.src.Web.user.models;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PatchMyUserInfoReq {
    private String profileImageURL;
    private String phoneNum;
    private String email;
    /**
     * 개인 사업자용
     */
    private String privateBusinessName;
    private String businessNumber;
    private String businessImageURL;

    /**
     * 법인 사업자용
     */
    private String corpBusinessName;
    //private String corpBusinessNumber;
}
