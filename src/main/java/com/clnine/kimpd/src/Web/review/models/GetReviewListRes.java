package com.clnine.kimpd.src.Web.review.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetReviewListRes {
    private final int reviewIdx;
    private final int reviewerUserIdx;
    private final String reviewNickname;
    private final String reviewUserImageProfile;
    private final int star;
    private final String description;
    private final String reviewMadeTime;
}
