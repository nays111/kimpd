package com.clnine.kimpd.src.Web.project.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PatchProjectReq {
    private String projectName;
    private String projectMaker;
    private String projectStartDate;
    private String projectEndDate;
    private String projectManager;
    private String projectDescription;
    private String projectFileURL;
    private String projectBudget;
}
