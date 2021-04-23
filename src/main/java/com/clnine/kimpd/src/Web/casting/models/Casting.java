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

//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name="reviewIdx")
//    private Review review;

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

    /**
     * castingStatus=0 : 장바구니에 담긴 상태
     * castingStatus=1 : 섭외 신청한 상태
     * castingStatus=2 : 섭외 승인한 상태
     * castingStatus=3 : 섭외 거절한 상태
     * castingStatus=4 : 작업 완료한 상태
     */
    @Column(name="castingStatus",length = 1)
    private int castingStatus=1;


    @Column(name="contractFileUrl",columnDefinition = "TEXT")
    private String contractFileUrl; //계약서 파일 firebase url

    private String status="ACTIVE";

    //섭외 신청하는 경우 생성자
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

    //장바구니의 담는 경우 생성자
    public Casting(UserInfo userInfo, UserInfo expertInfo, Project project, int castingStatus) {
        this.userInfo =userInfo;
        this.expert = expertInfo;
        this.project = project;
        this.castingStatus = castingStatus;
    }
}
