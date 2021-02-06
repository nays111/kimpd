package com.clnine.kimpd.src.Web.project.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetProjectRes {
    private final int projectIdx;
    private final String projectName;
    private final String projectMaker;
    private final String projectDescription;
    private final String projectStartDate;
    private final String projectEndDate;
}
