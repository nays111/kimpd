package com.clnine.kimpd.src.WebAdmin.banner.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@Getter
@AllArgsConstructor
public class AdminGetBannersRes {
    private final int bannerIdx;
    private final String bannerImageURL;
    private final String bannerLink;
    private final String startDate;
    private final String endDate;
    private final String status;

}
