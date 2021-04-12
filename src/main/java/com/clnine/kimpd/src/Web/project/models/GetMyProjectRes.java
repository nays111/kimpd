package com.clnine.kimpd.src.Web.project.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetMyProjectRes {
    private final int projectIdx;
    private final String projectName;
    private final String projectMaker;
    private final String projectStartDate;
    private final String projectEndDate;
    private final String projectManager;
    private final String projectDescription;
    private final String projectFileURL;
    private final String projectBudget;
}
