package com.clnine.kimpd.src.Web.casting.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetMyCastingRes {
    int userIdx; //전문가 인덱스
    String nickname;
    String profileImageURL;
    //카테고리 이름 여러개중 어떤게 띄어져야하나
    String categoryJobName;
    String introduce;
    int castingIdx;
    String castingStatus;
    String castingTerm;
    String projectName;
    String castingPrice;
}
