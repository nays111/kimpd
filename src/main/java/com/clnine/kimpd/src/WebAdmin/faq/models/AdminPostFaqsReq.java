package com.clnine.kimpd.src.WebAdmin.faq.models;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PUBLIC) // Unit Test 를 위해 PUBLIC
@Getter
public class AdminPostFaqsReq {
    private String faqQuestion;
    private String faqAnswer;
}
