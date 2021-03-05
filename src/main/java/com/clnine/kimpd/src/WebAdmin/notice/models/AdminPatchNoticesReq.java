package com.clnine.kimpd.src.WebAdmin.notice.models;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PUBLIC) // Unit Test 를 위해 PUBLIC
@Getter
public class AdminPatchNoticesReq {
    private int noticeIdx;
    private String noticeTitle;
    private String noticeDescription;
    private String status;
}
