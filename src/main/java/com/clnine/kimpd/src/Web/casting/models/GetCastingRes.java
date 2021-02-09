package com.clnine.kimpd.src.Web.casting.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetCastingRes {
    private final int  castingIdx;
    private final int projectIdx;
    private final String projectName;
    private final String projectMaker;
    private final String projectStartDate;
    private final String projectEndDate;
    private final String projectManager;
    private final String projectDescription;
    private final String projectFileURL;
    private final String castingPrice;
    private final String castingStartDate;
    private final String castingEndDate;
    private final String castingPriceDate;
    private final String castingWork;
    private final String castingMessage;
}
