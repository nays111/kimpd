package com.clnine.kimpd.src.WebAdmin.review.models;

import com.clnine.kimpd.config.BaseEntity;
import com.clnine.kimpd.src.WebAdmin.casting.models.AdminCasting;
import com.clnine.kimpd.src.WebAdmin.user.models.AdminUserInfo;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Table(name="Review")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class AdminReview extends BaseEntity {

    @Id
    @Column(name="reviewIdx",nullable = false,updatable = false)
    private int reviewIdx;

    @Column(name="star",length = 1)
    private float star;

    /**
     * 평가한 사람
     */
    @Column(name="description")
    private String description;

    /**
     * 평가한 사람
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="evaluateUserIdx",referencedColumnName = "userIdx")
    private AdminUserInfo evaluateUserInfo;

    /**
     * 평가당한 사람
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="evaluatedUserIdx",referencedColumnName = "userIdx")
    private AdminUserInfo evaluatedUserInfo;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="castingIdx")
    private AdminCasting casting;

    private String status="ACTIVE";

}
