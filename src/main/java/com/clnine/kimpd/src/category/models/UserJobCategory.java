package com.clnine.kimpd.src.category.models;

import com.clnine.kimpd.src.user.models.UserInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Data
@Entity
@Table(name="UserJobCategory")
public class UserJobCategory {

    @Id @Column(name="userJobCategoryIdx",nullable = false,updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userJobCategoryIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="userIdx")
    private UserInfo userInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="jobCategoryParentIdx")
    private JobCategoryParent jobCategoryParent;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="jobCategoryChildIdx")
    private JobCategoryChild jobCategoryChild;

    public UserJobCategory(UserInfo userInfo, JobCategoryParent jobCategoryParent, JobCategoryChild jobCategoryChild) {
        this.userInfo = userInfo;
        this.jobCategoryParent = jobCategoryParent;
        this.jobCategoryChild = jobCategoryChild;
    }
}
