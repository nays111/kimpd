package com.clnine.kimpd.src.Web.review.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetMyReviewsRes {
    int userIdx;
    String nickname;
    String profileImageURL;
    String categoryJobName;
    String introduce;
    int castingIdx;
    String castingTerm;
    String projectName;
    String castingPrice;
    String reviewState;
    float star;

}
