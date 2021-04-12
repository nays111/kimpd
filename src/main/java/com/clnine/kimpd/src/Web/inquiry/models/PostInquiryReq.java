package com.clnine.kimpd.src.Web.inquiry.models;

import com.sun.istack.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;

@Getter
@AllArgsConstructor
public class PostInquiryReq {
    private Integer inquiryCategoryIdx;
    private String inquiryTitle;
    private String inquiryDescription;
    private List<String> inquiryFileUrlList;

}
