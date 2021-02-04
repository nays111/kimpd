package com.clnine.kimpd.src.basket.models;

import com.clnine.kimpd.config.BaseEntity;
import com.clnine.kimpd.src.project.models.Project;
import com.clnine.kimpd.src.user.models.UserInfo;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PUBLIC) // Unit Test 를 위해 PUBLIC
@EqualsAndHashCode(callSuper = false)
@Entity
@Data
@Table(name="Basket")
public class Basket extends BaseEntity {
    @Id
    @Column(name="basketIdx",nullable = false,updatable = false)
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private int basketIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="userIdx")
    private UserInfo userInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="projectIdx")
    private Project project;

    @Column(name="status")
    private String status = "ACTIVE";

}
