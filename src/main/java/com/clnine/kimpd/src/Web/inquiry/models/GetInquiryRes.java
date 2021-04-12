package com.clnine.kimpd.src.Web.inquiry.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class GetInquiryRes {
    private final String inquiryTitle;
    private final String inquiryDescription;
    private final List<String> inquiryFileUrlList;
    private final String userNickname;
    private final String createdDate;
    private final String inquiryAnswer;
    private final String answerDate;

}
