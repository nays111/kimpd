package com.clnine.kimpd.src.WebAdmin.inquiry.models;

import com.clnine.kimpd.config.BaseEntity;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@Table(name="InquiryFile")
public class AdminInquiryFile extends BaseEntity {
    @Id @Column(name="inquiryFileIdx",nullable = false,updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int inquiryCategoryIdx;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="inquiryIdx")
    private AdminInquiry adminInquiry;


    @Column(name="inquiryFileName")
    private String inquiryFileName;

    @Builder
    public AdminInquiryFile(AdminInquiry adminInquiry, String inquiryFileName){
        this.adminInquiry = adminInquiry;
        this.inquiryFileName = inquiryFileName;
    }

}
