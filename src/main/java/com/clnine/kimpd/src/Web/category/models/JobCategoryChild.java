package com.clnine.kimpd.src.Web.category.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Data
@Entity
@Table(name="JobCategoryChild")
public class JobCategoryChild {
    @Id
    @Column(name="jobCategoryChildIdx",nullable = false,updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int jobCategoryChildIdx;

    @Column(name="jobCategoryChildName")
    private String jobCategoryChildName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="jobCategoryParentIdx")
    private JobCategoryParent jobCategoryParent;
}
