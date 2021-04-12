package com.clnine.kimpd.src.Web.casting.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class GetMyReceivedCastingsRes {
    int totalCount;
    List<GetMyReceivedCastingDTO> getMyReceivedCastingResList;
}
