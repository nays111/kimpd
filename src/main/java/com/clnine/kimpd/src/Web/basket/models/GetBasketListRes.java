package com.clnine.kimpd.src.Web.basket.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class GetBasketListRes {
    private final List<GetBasketsRes> getBasketsResList;
    private final String projectBudget;
    private final int castingExpertCount;
    private final String totalCastingPrice;

}
