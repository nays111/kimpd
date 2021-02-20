package com.clnine.kimpd.src.Web.basket.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetBasketsRes {
    private final int castingIdx;
    private final int userIdx;
    private final String profileImageURL;
    private final String nickname;
    private final String jobCategoryChildName;
    private final String introduce;
    private final String castingDate;
    private final String projectName;
    private final String castingPrice;
    private final String castingCondition;
}
