package com.clnine.kimpd.src.WebAdmin.notice.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AdminGetNoticesRes {
    private final int noticeIdx;
    private final String noticeTitle;
    private final String noticeDescription;
    private final String status;

}
