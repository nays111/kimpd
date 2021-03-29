package com.clnine.kimpd.src.Web.contract.models;

import com.clnine.kimpd.config.BaseEntity;
import com.clnine.kimpd.src.Web.casting.models.Casting;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Table(name="Contract")
public class Contract extends BaseEntity {
    @Id
    @Column(name="contractIdx",nullable=false,updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int contractIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="castingIdx")
    private Casting casting;

    @Column(name="contractContent")
    private String contractContent;

    @Column(name="status")
    private String status = "ACTIVE";

}
