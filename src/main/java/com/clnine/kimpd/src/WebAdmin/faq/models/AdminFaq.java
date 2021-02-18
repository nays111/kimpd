package com.clnine.kimpd.src.WebAdmin.faq.models;

import com.clnine.kimpd.config.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@Table(name="Faq")
public class AdminFaq extends BaseEntity {
    @Id @Column(name="faqIdx",nullable = false,updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int faqIdx;

    @Column(name="faqQuestion")
    private String faqQuestion;

    @Column(name="faqAnswer")
    private String faqAnswer;

    private String status="ACTIVE";

    public AdminFaq(String faqQuestion, String faqAnswer) {
        this.faqQuestion = faqQuestion;
        this.faqAnswer = faqAnswer;
    }
}
