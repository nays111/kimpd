package com.clnine.kimpd.src.Web.inquiry.models;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Table(name="InquiryFile")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class InquiryFile {
    @Id
    @Column(name="inquiryFileIdx",nullable = false,updatable = false)
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private int inquiryFileIdx;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="inquiryIdx")
    private Inquiry inquiry;


    @Column(name="inquiryFileName")
    private String inquiryFileName;

    @Builder
    public InquiryFile(Inquiry inquiry, String inquiryFileName) {
        this.inquiry = inquiry;
        this.inquiryFileName = inquiryFileName;
    }
}
