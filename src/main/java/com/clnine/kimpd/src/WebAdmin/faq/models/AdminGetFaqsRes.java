package com.clnine.kimpd.src.WebAdmin.faq.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AdminGetFaqsRes {
    private final int faqIdx;
    private final String faqQuestion;
    private final String faqAnswer;
    private final String status;

}
