package com.clnine.kimpd.src.WebAdmin.report.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AdminGetReportsRes {
    private final int reportIdx;
    private final String reportUserNickname;
    private final String reportedUserNickname;
    private final String reportCategoryName;
    private final String createdAt;
    private final String status;
}
