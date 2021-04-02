package com.clnine.kimpd.src.Web.review.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostReviewReq {
    private final int evaluatedUserIdx;
    private final float star;
    private final String description;

}
