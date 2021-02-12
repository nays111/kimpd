package com.clnine.kimpd.src.Web.review.models;

import com.clnine.kimpd.config.BaseEntity;
import com.clnine.kimpd.src.Web.user.models.UserInfo;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Table(name="Review")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Review extends BaseEntity {

    @Id
    @Column(name="reviewIdx",nullable = false,updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reviewIdx;

    @Column(name="star")
    private float star;

    @Column(name="description")
    private String description;

    /**
     * 평가한 사람
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="evaluateUserIdx",referencedColumnName = "userIdx")
    private UserInfo evaluateUserInfo;
    /**
     * 평가당한 사람
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="evaluatedUserIdx",referencedColumnName = "userIdx")
    private UserInfo evaluatedUserInfo;

    private String status="ACTIVE";

    public Review(float star, String description, UserInfo userInfo, UserInfo expertInfo) {
        this.star = star;
        this.description = description;
        this.evaluateUserInfo = userInfo;
        this.evaluatedUserInfo = expertInfo;
    }
}
