package com.clnine.kimpd.src.Web.expert.models;

import com.clnine.kimpd.src.Web.review.models.GetReviewListRes;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class GetExpertRes {
    private final Integer userIdx;
    private final String profileImageURL;
    private final String nickname;
    private final String city;
    private final String jobCategoryParentName; //-> 직종이 여러개일 수 있는데 어떻게할지 아직 미정
    private final Double reviewAverage;
    private final int reviewCount;
    private final String introduce;
    private final String career;
    private final String etc;
    private final List<GetPortfolioListRes> getPortfolioResList;
    private final List<GetReviewListRes> getReviewResList;
    private final int projectCount;
}
