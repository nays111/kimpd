package com.clnine.kimpd.src.WebAdmin.inquiry.models;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PUBLIC) // Unit Test 를 위해 PUBLIC
@Getter
public class AdminPatchInquiriesReq {
    private int inquiryIdx;
    private String inquiryAnswer;
    private String status;
}
