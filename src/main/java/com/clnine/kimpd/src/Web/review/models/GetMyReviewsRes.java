package com.clnine.kimpd.src.Web.review.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class GetMyReviewsRes {
    private int totalCount;
    private List<GetMyReviewsListDTO> getMyReviewsList;
}
