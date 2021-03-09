package com.clnine.kimpd.src.WebAdmin.casting.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.Column;
import java.util.Date;

@Getter
@AllArgsConstructor
public class AdminGetCastingRes {
    private final String projectName;
    private final String projectMaker;
    private final String projectManager;
    private final String projectDescription;
    private final String projectFileURL;
    private final String userName;
    private final String expertName;
    private final String castingPrice;
    private final String castingPriceDate;
    private final String castingMessage;
    private final String castingStartDate;
    private final String castingEndDate;
    private final String castingWork;
    private final String castingStatus;
    private final String status;
}
