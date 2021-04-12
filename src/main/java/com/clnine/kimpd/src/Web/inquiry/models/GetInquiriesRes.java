package com.clnine.kimpd.src.Web.inquiry.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class GetInquiriesRes {
    final int totalCount;
    final List<GetInquiriesDTO> getInquiriesList;
}
