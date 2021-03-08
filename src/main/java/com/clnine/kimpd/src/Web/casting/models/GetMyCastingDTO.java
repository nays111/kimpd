package com.clnine.kimpd.src.Web.casting.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetMyCastingDTO {
    int userIdx; //전문가 인덱스
    String nickname;
    String profileImageURL;
    String categoryJobName;
    String introduce;
    int castingIdx;
    String castingStatus;
    String castingTerm;
    String projectName;
    String castingPrice;
}
