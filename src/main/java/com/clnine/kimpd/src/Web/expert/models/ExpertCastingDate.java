package com.clnine.kimpd.src.Web.expert.models;

import com.clnine.kimpd.src.Web.user.models.UserInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Table(name="ExpertCastingDate")
@NoArgsConstructor
public class ExpertCastingDate {
    @Id
    @Column(name="userCastingDateIdx",nullable = false,updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userCastingDateIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="userIdx")
    private UserInfo userInfo;

    @Column(name="castingPossibleDate")
    private String castingPossibleDate;

    public ExpertCastingDate(UserInfo userInfo, String castingPossibleDate) {
        this.userInfo = userInfo;
        this.castingPossibleDate = castingPossibleDate;
    }
}
