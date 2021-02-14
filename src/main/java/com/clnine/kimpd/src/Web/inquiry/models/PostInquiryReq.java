package com.clnine.kimpd.src.Web.inquiry.models;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PostInquiryReq {
    private int inquiryCategoryIdx;
    private String inquiryTitle;
    private String inquiryDescription;
    private List<String> inquiryFileList;
}
