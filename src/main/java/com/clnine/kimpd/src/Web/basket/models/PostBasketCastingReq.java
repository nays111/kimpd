package com.clnine.kimpd.src.Web.basket.models;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 장바구니에 있는걸 선택해서 섭외 요청 보낼 때 사용되는 DTO
 */
@Getter
@NoArgsConstructor
public class PostBasketCastingReq {
    List<Integer> castingIdx;
}
