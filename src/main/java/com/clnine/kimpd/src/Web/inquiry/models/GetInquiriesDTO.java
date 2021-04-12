package com.clnine.kimpd.src.Web.inquiry.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetInquiriesDTO {
    private final int no;
    private final int inquiryIdx;
    private final String answerStatus;
    private final String inquiryTitle;
    private final String userNickname;
    private final String createdDate;
}
