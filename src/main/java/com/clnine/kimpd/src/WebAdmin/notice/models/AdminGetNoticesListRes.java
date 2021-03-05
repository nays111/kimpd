package com.clnine.kimpd.src.WebAdmin.notice.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class AdminGetNoticesListRes {
    List<AdminGetNoticesRes> noticesList;
}