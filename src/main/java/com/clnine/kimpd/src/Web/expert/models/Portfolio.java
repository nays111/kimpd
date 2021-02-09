package com.clnine.kimpd.src.Web.expert.models;

import com.clnine.kimpd.config.BaseEntity;
import com.clnine.kimpd.src.Web.user.models.UserInfo;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Table(name="Portfolio")
@RequiredArgsConstructor
public class Portfolio extends BaseEntity {

    @Id
    @Column(name="portfolioIdx",nullable = false,updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int portfolioIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="userIdx")
    private UserInfo userInfo;

    @Column(name="portfolioURL",nullable = false)
    private String portfolioURL;

    @Column(name="status")
    private String status = "ACTIVE";
}
