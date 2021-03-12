package com.clnine.kimpd.src.WebAdmin.report.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AdminGetReportRes {
    private final String reportUserName;
    private final String reportUserNickname;
    private final String reportUserPhoneNumber;
    private final String reportUserEmail;
    private final String reportedUserName;
    private final String reportedUserNickname;
    private final String reportedUserPhoneNumber;
    private final String reportedUserEmail;
    private final String reportCategoryName;
    private final String reportDescription;
    private final String createdAt;
    private final String status;
}
