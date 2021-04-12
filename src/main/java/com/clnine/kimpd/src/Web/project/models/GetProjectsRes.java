package com.clnine.kimpd.src.Web.project.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class GetProjectsRes {
    private final int totalCount;
    private final List<GetProjectsDTO> getProjectsList;
}
