package com.clnine.kimpd.src.Web.report.models;

import com.clnine.kimpd.config.BaseEntity;
import com.clnine.kimpd.src.Web.user.models.UserInfo;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name="Report")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Report extends BaseEntity {
    @Id
    @Column(name="reportIdx",nullable = false,updatable = false)
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private int reportIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="reportUserIdx",referencedColumnName = "userIdx")
    private UserInfo reportUserInfo; //신고한 사람

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="reportedUserIdx",referencedColumnName = "userIdx")
    private UserInfo reportedUserInfo;//신고당한 사람

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="reportCategoryIdx")
    private ReportCategory reportCategory;

    @Column(name="reportDescription")
    private String reportDescription;

    @Column(name="status")
    private String status="ACTIVE";
}
