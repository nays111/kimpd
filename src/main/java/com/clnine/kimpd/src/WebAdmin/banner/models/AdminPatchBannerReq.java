package com.clnine.kimpd.src.WebAdmin.banner.models;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PUBLIC) // Unit Test 를 위해 PUBLIC
@Getter
public class AdminPatchBannerReq {
    private int bannerIdx;
    private String bannerImageURL;
    private String bannerLink;
    private String startDate;
    private String endDate;
    private String status;

}
