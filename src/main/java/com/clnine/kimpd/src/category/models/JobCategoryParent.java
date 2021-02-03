package com.clnine.kimpd.src.category.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Data
@Entity
@Table(name="JobCategoryParent")
public class JobCategoryParent {
    @Id
    @Column(name="jobCategoryParentIdx",nullable = false,updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int jobCategoryParentIdx;

    @Column(name="jobCategoryName")
    private String jobCategoryName;
}
