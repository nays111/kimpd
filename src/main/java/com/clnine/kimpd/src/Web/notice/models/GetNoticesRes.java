package com.clnine.kimpd.src.Web.notice.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
@Getter
@AllArgsConstructor
public class GetNoticesRes {
    private int totalCount;
    private List<GetNoticesDTO> getNoticesList;
}
