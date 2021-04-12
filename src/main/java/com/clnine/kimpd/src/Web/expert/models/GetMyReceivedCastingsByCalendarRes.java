package com.clnine.kimpd.src.Web.expert.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetMyReceivedCastingsByCalendarRes {
    private final int userIdx;
    private final String userProfileImageUrl;
    private final String name;
    private final String nickname;

    private final String castingDate;
    private final String castingPrice;
    private final String projectName;
    private final String projectDescription;
    private final String projectMaker;
    private final String projectManager;
    private final String castingPriceDate;
    private final String castingMessage;
}
