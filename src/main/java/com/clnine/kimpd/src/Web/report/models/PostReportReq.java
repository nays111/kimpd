package com.clnine.kimpd.src.Web.report.models;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class PostReportReq {
    private Integer reportCategoryIdx;
    private String reportDescription;
}
