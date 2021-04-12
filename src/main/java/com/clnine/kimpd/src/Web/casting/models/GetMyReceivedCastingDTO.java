package com.clnine.kimpd.src.Web.casting.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetMyReceivedCastingDTO {
    int userIdx;
    String nickname;
    String profileImageURL;
    String projectName;
    int castingIdx;
    String castingStatus;
    String reviewStatus;
    String castingTerm;
    String castingDate;
    String castingPrice;
}
