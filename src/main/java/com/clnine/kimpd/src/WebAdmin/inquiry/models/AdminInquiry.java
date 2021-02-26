package com.clnine.kimpd.src.WebAdmin.inquiry.models;

import com.clnine.kimpd.config.BaseEntity;
import com.clnine.kimpd.src.WebAdmin.user.models.AdminUserInfo;
import com.sun.security.jgss.InquireType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@Table(name="Inquiry")
public class AdminInquiry extends BaseEntity {
    @Id @Column(name="inquiryIdx",nullable = false,updatable = false)
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
    private AdminInquiryCategory adminInquiryCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="userIdx")
    private AdminUserInfo adminUserInfo;

    private String status="ACTIVE";

    public AdminInquiry(String inquiryTitle, String inquiryDescription, AdminInquiryCategory adminInquiryCategory, AdminUserInfo adminUserInfo) {
        this.inquiryTitle = inquiryTitle;
        this.inquiryDescription = inquiryDescription;
        this.adminInquiryCategory = adminInquiryCategory;
        this.adminUserInfo = adminUserInfo;
    }
}
