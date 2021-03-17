package com.clnine.kimpd.src.WebAdmin.contract.models;

import com.clnine.kimpd.config.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@Table(name="Contract")
public class AdminContract extends BaseEntity {
    @Id @Column(name="contractIdx",nullable = false,updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int contractIdx;

    @Column(name="contractContent")
    private String contractContent;

    private String status="ACTIVE";

    public AdminContract(String contractContent) {
        this.contractContent = contractContent;
    }
}
