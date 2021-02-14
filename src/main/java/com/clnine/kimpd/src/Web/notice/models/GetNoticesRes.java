package com.clnine.kimpd.src.Web.notice.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetNoticesRes {
    private int noticeIdx;
    private String noticeTitle;
    private String noticeDescription;
    private String createdDate;
}
