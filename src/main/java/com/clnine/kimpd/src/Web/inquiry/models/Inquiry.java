package com.clnine.kimpd.src.Web.inquiry.models;

import com.clnine.kimpd.config.BaseEntity;
import com.clnine.kimpd.src.Web.report.models.ReportCategory;
import com.clnine.kimpd.src.Web.user.models.UserInfo;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Table(name="Inquiry")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Inquiry extends BaseEntity {
    @Id
    @Column(name="inquiryIdx",nullable = false,updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int inquiryIdx;

    @Column(name="inquiryTitle")
    private String inquiryTitle;

    @Column(name="inquiryDescription")
    private String inquiryDescription;

    @Column(name="inquiryAnswer",nullable = true)
    private String inquiryAnswer;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="inquiryCategoryIdx")
    private InquiryCategory inquiryCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="userIdx")
    private UserInfo userInfo;

    @Column(name="status")
    private String status = "ACTIVE";

    @Builder
    public Inquiry(String title, String description, InquiryCategory inquiryCategory, UserInfo userInfo) {
        this.inquiryTitle =title;
        this.inquiryDescription = description;
        this.inquiryCategory = inquiryCategory;
        this.userInfo = userInfo;
    }
}
