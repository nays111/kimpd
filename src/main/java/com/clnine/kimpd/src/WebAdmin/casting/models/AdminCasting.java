package com.clnine.kimpd.src.WebAdmin.casting.models;

import com.clnine.kimpd.config.BaseEntity;
import com.clnine.kimpd.src.WebAdmin.review.models.AdminReview;
import com.clnine.kimpd.src.WebAdmin.user.models.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@Table(name="Casting")
public class AdminCasting extends BaseEntity {
    @Id @Column(name="castingIdx",nullable = false,updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int castingIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="expertIdx", referencedColumnName = "userIdx")
    private AdminUserInfo adminExpertInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="userIdx", referencedColumnName = "userIdx")
    private AdminUserInfo adminUserInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="projectIdx")
    private AdminProject adminProject;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="reviewIdx")
    private AdminReview review;


    @Column(name="castingPrice")
    private String castingPrice;

    @Column(name="castingPriceDate")
    private String castingPriceDate;

    @Column(name="castingMessage")
    private String castingMessage;

    @Column(name="castingStartDate")
    private String castingStartDate;

    @Column(name="castingEndDate")
    private String castingEndDate;

    @Column(name="castingWork")
    private String castingWork;

    @Column(name="castingStatus")
    private int castingStatus = 1;

    @Column(name="status")
    private String status="ACTIVE";

    public AdminCasting(AdminUserInfo adminExpertInfo, AdminUserInfo adminUserInfo,
                        AdminProject adminProject, String castingPrice, String castingPriceDate,
                        String castingMessage, String castingStartDate, String castingEndDate,
                        String castingWork, int castingStatus, String status) {

        this.adminExpertInfo = adminExpertInfo;
        this.adminUserInfo = adminUserInfo;
        this.adminProject = adminProject;
        this.castingPrice = castingPrice;
        this.castingPriceDate = castingPriceDate;
        this.castingMessage = castingMessage;
        this.castingStartDate = castingStartDate;
        this.castingEndDate = castingEndDate;
        this.castingWork = castingWork;
        this.castingStatus = castingStatus;
        this.status = status;
    }
}
