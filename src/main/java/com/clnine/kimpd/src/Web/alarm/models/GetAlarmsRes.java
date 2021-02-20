package com.clnine.kimpd.src.Web.alarm.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetAlarmsRes {
    private final int alarmIdx;
    private final String alarmDay;
    private final String profileImageURL;
    private final int readStatus;
    private final String message;
    private final String alarmTime;
}
