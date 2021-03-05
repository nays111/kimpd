package com.clnine.kimpd.src.WebAdmin.inquiry.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class AdminGetInquiriesListRes {
    List<AdminGetInquiriesRes> inquiryList;
}