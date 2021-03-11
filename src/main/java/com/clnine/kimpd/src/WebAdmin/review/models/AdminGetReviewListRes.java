package com.clnine.kimpd.src.WebAdmin.review.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class AdminGetReviewListRes {
    List<AdminGetReviewsRes> reviewList;
}