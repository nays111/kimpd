package com.clnine.kimpd.src.Web.expert.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;

@Getter
@AllArgsConstructor
public class GetUsersRes {
    private final int userIdx;
    private final String profileImageURL;
    private final String nickname;

    private final String jobCategoryChildName;
    private final String introduce;
    private final float reviewAverage;
    private final int reviewCount;


}
