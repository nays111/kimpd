package com.clnine.kimpd.src.Web.casting.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PatchCastingReq {
    private String castingPrice;
    private String castingStartDate;
    private String castingEndDate;
    private String castingPriceDate;
    private String castingWork;
    private String castingMessage;
}
