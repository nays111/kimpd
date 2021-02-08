package com.clnine.kimpd.src.Web.user.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;

@Getter
@AllArgsConstructor
public class GetUsersRes {
    private final Integer userIdx;
    private final String profileImageURL;
    private final String nickname;

    //private final ArrayList<String> jobCategoryParentName;
    private final String introduce;
    private final BigDecimal reviewAverage;
    private final Integer reviewCount;

}
