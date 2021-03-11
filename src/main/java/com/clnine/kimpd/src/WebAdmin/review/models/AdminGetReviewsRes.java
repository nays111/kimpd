package com.clnine.kimpd.src.WebAdmin.review.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AdminGetReviewsRes {
    private final int reviewIdx;
    private final String reviewStatus;
    private final String evaluateUserNickname;
    private final String evaluatedUserNickname;
    private final float star;
    private final String status;
}
