package com.clnine.kimpd.src.review.models;

import com.clnine.kimpd.config.BaseEntity;
import com.clnine.kimpd.src.user.models.UserInfo;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Table(name="Review")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class review extends BaseEntity {

    @Id
    @Column(name="reviewIdx",nullable = false,updatable = false)
    private int reviewIdx;

    @Column(name="star",length = 1)
    private int star;

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

}
