package com.clnine.kimpd.src.Web.expert.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class GetMyExpertRes {
    private List<UserJobCategoryParentDTO> jobCategoryParentList;
    private List<UserJobCategoryChildDTO> jobCategoryChildList;
    private List<UserGenreCategoryDTO> genreCategoryList;
    private String introduce;
    private String career;
    private List<String> portfolioFileURL;
    private String etc;
    private Integer minimumCastingPrice;
    private List<String> castingPossibleDateList;
    private int agreeShowDB;
}
