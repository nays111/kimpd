package com.clnine.kimpd.src.Web.faq.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetFaqsRes {
    private int faqIdx;
    private String faqQuestion;
    private String faqAnswer;
}
