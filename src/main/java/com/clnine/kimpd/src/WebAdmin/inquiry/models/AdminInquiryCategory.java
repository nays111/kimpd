package com.clnine.kimpd.src.WebAdmin.inquiry.models;

import com.clnine.kimpd.config.BaseEntity;
import com.clnine.kimpd.src.WebAdmin.user.models.AdminUserInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@Table(name="InquiryCategory")
public class AdminInquiryCategory extends BaseEntity {
    @Id @Column(name="inquiryCategoryIdx",nullable = false,updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int inquiryCategoryIdx;

    @Column(name="inquiryCategoryName")
    private String inquiryCategoryName;
}
