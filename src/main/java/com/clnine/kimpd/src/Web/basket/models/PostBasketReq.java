package com.clnine.kimpd.src.Web.basket.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 전문가를 장바구니에 담을 때 사용하는 DTO
 */
@Getter
@NoArgsConstructor
public class PostBasketReq {
    List<Integer> userIdx;
    String castingStartDate;
    String castingEndDate;
    String castingPrice;
    int projectIdx;
}
