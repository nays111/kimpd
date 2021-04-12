package com.clnine.kimpd.src.Web.project.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetProjectsDTO {
    private final int projectIdx;
    private final String projectName;
    private final String projectDescription;
    private final String projectDate;
    private final String projectBudget;
}
