package com.clnine.kimpd.src.Web.casting.models;

import com.clnine.kimpd.src.Web.user.models.GetUserRes;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class GetMyCastingsRes {
    int totalCount;
    List<GetMyCastingDTO> getMyCastingResList;
}
