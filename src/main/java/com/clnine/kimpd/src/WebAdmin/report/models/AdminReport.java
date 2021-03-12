package com.clnine.kimpd.src.WebAdmin.report.models;

import com.clnine.kimpd.config.BaseEntity;
import com.clnine.kimpd.src.WebAdmin.casting.models.AdminCasting;
import com.clnine.kimpd.src.WebAdmin.user.models.AdminUserInfo;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Table(name="Report")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class AdminReport extends BaseEntity {

    @Id
    @Column(name="reportIdx",nullable = false,updatable = false)
    private int reportIdx;

    /**
     * 신고한 사람
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="reportUserIdx",referencedColumnName = "userIdx")
    private AdminUserInfo reportUserInfo;

    /**
     * 신고당한 사람
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="reportedUserIdx",referencedColumnName = "userIdx")
    private AdminUserInfo reportedUserInfo;

    /**
     * 신고 카테고리
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="reportCategoryIdx",referencedColumnName = "reportCategoryIdx")
    private AdminReportCategory reportCategory;

    /**
     * 신고 사유
     */
    @Column(name="reportDescription")
    private String reportDescription;

    private String status="ACTIVE";

}
