package com.clnine.kimpd.src.Web.expert.models;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class PostExpertsReq {
    private String word;
    private List<Long> jobCategoryParentIdx;
    private List<Long> jobCategoryChildIdx;
    private List<Long> genreCategoryIdx;
    private List<String> city;
//    private String castingStartDate;
//    private String castingEndDate;
    private Integer minimumCastingPrice;
    private List<String> castingDate;
    private Integer page;
    private Integer sort;
}
