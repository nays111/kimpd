package com.clnine.kimpd.src.Web.faq.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class GetFaqsRes {
    private int totalCount;
    private List<GetFaqsDTO> getFaqsList;
}
