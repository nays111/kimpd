package com.clnine.kimpd.src.Web.report.models;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Entity
@Data
@Table(name="ReportCategory")
public class ReportCategory {
    @Id
    @Column(name="reportCategoryIdx",nullable = false,updatable = false)
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private int reportCategoryIdx;

    @Column(name="reportCategoryName")
    private String reportCategoryName;



}
