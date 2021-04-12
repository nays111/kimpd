package com.clnine.kimpd.src.Web.faq.models;

import com.clnine.kimpd.config.BaseEntity;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name="Faq")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Faq extends BaseEntity {
    @Id
    @Column(name="faqIdx",nullable = false,updatable = false)
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private int faqIdx;

    @Column(name="faqQuestion")
    private String faqQuestion;

    @Column(name="faqAnswer")
    private String faqAnswer;

    @Column(name="status")
    private String status="ACTIVE";
}
