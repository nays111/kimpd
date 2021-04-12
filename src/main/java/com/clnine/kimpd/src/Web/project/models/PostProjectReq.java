package com.clnine.kimpd.src.Web.project.models;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PUBLIC) @Getter
public class PostProjectReq {
    private String projectName;
    private String projectMaker;
    private String projectStartDate;
    private String projectEndDate;
    private String projectManager;
    private String projectDescription;
    private String projectFileURL;
    private String projectBudget;
}
