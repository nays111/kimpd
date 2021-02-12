package com.clnine.kimpd.src.Web.casting.models;

import com.clnine.kimpd.config.BaseEntity;
import com.clnine.kimpd.src.Web.project.models.Project;
import com.clnine.kimpd.src.Web.review.models.Review;
import com.clnine.kimpd.src.Web.user.models.UserInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity
@Data
@Table(name="Casting")
@NoArgsConstructor
public class Casting extends BaseEntity {
    @Id
    @Column(name="castingIdx",nullable = false,updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int castingIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="expertIdx",referencedColumnName = "userIdx")
    private UserInfo expert;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="userIdx",referencedColumnName = "userIdx")
    private UserInfo userInfo;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="reviewIdx")
    private Review review;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="projectIdx")
    private Project project;

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
    private int castingStatus=1;

    private String status="ACTIVE";

    public Casting(UserInfo userInfo, UserInfo expertInfo, Project project, String castingPrice, String castingStartDate, String castingEndDate, String castingWork, String castingPriceDate, String castingMessage) {
        this.userInfo =userInfo;
        this.expert = expertInfo;
        this.project = project;
        this.castingPrice = castingPrice;
        this.castingStartDate = castingStartDate;
        this.castingEndDate = castingEndDate;
        this.castingMessage = castingMessage;
        this.castingWork = castingWork;
        this.castingPriceDate = castingPriceDate;
    }
}
