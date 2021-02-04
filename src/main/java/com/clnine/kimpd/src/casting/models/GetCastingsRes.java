package com.clnine.kimpd.src.casting.models;

import com.clnine.kimpd.src.user.models.GetUserRes;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class GetCastingsRes {
    GetUserRes getUserRes;
    CastingCountRes castingCountRes;
    List<GetMyCastingRes> getMyCastingResList;
}
