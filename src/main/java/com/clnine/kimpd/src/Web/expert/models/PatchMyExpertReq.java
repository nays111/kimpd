package com.clnine.kimpd.src.Web.expert.models;


import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class PatchMyExpertReq {
    private List<Integer> jobCategoryParentIdx;
    private List<Integer> jobCategoryChildIdx;
    private List<Integer> genreCategoryIdx;
    private String introduce;
    private String career;
    private List<String> portfolioFileURL;
    private String etc;
    private Integer minimumCastingPrice;
    private List<String> castingPossibleDate;
//    private String castingStartPossibleDate;
//    private String castingEndPossibleDate;
    private int agreeShowDB;
}
