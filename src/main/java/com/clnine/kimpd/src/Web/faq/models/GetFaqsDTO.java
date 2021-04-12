package com.clnine.kimpd.src.Web.faq.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetFaqsDTO {
    private int faqIdx;
    private String faqQuestion;
    private String faqAnswer;
}
