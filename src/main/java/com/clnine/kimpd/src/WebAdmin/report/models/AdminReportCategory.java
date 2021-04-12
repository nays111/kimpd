package com.clnine.kimpd.src.WebAdmin.report.models;

import com.clnine.kimpd.config.BaseEntity;
import com.clnine.kimpd.src.WebAdmin.user.models.AdminUserInfo;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Table(name="ReportCategory")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class AdminReportCategory{

    @Id
    @Column(name="reportCategoryIdx",nullable = false,updatable = false)
    private int reportCategoryIdx;

    /**
     * 신고 카테고리 명
     */
    @Column(name="reportCategoryName")
    private String reportCategoryName;


}
