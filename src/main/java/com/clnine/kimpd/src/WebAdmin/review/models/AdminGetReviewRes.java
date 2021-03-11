package com.clnine.kimpd.src.WebAdmin.review.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AdminGetReviewRes {
    private final String projectName;
    private final String evaluateUserNickname;
    private final String evaluatedUserNickname;
    private final String startDate;
    private final String endDate;
    private final float star;
    private final String description;
    private final String status;
}
