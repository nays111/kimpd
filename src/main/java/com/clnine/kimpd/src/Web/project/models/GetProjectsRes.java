package com.clnine.kimpd.src.Web.project.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetProjectsRes {
    private final int projectIdx;
    private final String projectName;
    private final String projectDescription;
    private final String projectStartDate;
    private final String projectEndDate;
    private final String projectBudget;


}
