package com.clnine.kimpd.src.casting.models;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PUBLIC) // Unit Test 를 위해 PUBLIC
@Getter
public class PostCastingReq {
    private int expertIdx;
    private String projectName;
    private String projectMaker;
    private String projectStartDate;
    private String projectEndDate;
    private String projectDescription;
    private String projectFileURL;
    private String projectBudget;
    private String castingPrice;
    private String castingStartDate;
    private String castingEndDate;
    private String castingPriceDate;
    private String castingWork;
    private String castingMessage;
}
