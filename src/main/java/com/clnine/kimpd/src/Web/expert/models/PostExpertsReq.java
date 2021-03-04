package com.clnine.kimpd.src.Web.expert.models;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class PostExpertsReq {
    // 검색 API를 위한 DTO ( GET 대신 POST)로 사용
    private String word;
    private List<Long> jobCategoryParentIdx;
    private List<Long> jobCategoryChildIdx;
    private List<Long> genreCategoryIdx;
    private List<String> city;
    private String castingStartDate;
    private String castingEndDate;
    private Integer minimumCastingPrice;
    private Integer page;
    private Integer sort;
}
