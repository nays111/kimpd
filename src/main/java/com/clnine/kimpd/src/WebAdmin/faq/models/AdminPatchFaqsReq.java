package com.clnine.kimpd.src.WebAdmin.faq.models;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PUBLIC) // Unit Test 를 위해 PUBLIC
@Getter
public class AdminPatchFaqsReq {
    private int noticeIdx;
    private String noticeTitle;
    private String noticeDescription;
    private String status;
}
