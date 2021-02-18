package com.clnine.kimpd.src.WebAdmin.faq.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AdminGetFaqsRes {
    private final int noticeIdx;
    private final String noticeTitle;
    private final String noticeDescription;
    private final String status;

}
