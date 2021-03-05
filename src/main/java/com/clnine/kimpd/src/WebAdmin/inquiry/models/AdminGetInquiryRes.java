package com.clnine.kimpd.src.WebAdmin.inquiry.models;

import com.sun.istack.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class AdminGetInquiryRes {
    private final String inquiryCategoryName;
    private final String inquiryTitle;
    private final String nickname;
    private final String inquiryDescription;
    private final String inquiryAnswer;
    @Nullable
    private final ArrayList<String> inquiryFileList;
    private final String status;

}
