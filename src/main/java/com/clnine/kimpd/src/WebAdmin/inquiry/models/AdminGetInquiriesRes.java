package com.clnine.kimpd.src.WebAdmin.inquiry.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AdminGetInquiriesRes {
    private final int inquiryIdx;
    private final String answerState;
    private final String inquiryTitle;
    private final String nickname;
    private final String createdAt;
    private final String status;

}
