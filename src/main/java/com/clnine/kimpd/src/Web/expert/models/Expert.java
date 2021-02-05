package com.clnine.kimpd.src.Web.expert.models;

import com.clnine.kimpd.config.BaseEntity;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name="Expert")
public class Expert extends BaseEntity{

    @Id@Column(name="expertIdx",nullable = false,updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int expertIdx;

    private String introduce;

    private String career;

    private String etc;

    private int agreeShowDB;

}
