package com.clnine.kimpd.src.Web.casting.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetMyReceivedCastingRes {
    int userIdx; //섭외 요청 보낸사람인덱스
    String nickname; //todo 여기 제작사가 들어가야대는거같은데 다시 확인
    String profileImageURL;
    String projectName;
    int castingIdx;
    String castingStatus;
    String castingTerm;
    String castingDate;
    String castingPrice;
}
