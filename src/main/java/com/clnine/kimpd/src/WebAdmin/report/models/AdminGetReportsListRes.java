package com.clnine.kimpd.src.WebAdmin.report.models;

import com.clnine.kimpd.src.WebAdmin.inquiry.models.AdminGetInquiriesRes;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class AdminGetReportsListRes {
    List<AdminGetReportsRes> reportList;
}