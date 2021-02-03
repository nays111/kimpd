package com.clnine.kimpd.src.project.models;

import com.clnine.kimpd.config.BaseEntity;
import com.clnine.kimpd.src.user.models.UserInfo;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name="Project")
@RequiredArgsConstructor
public class Project extends BaseEntity {
    @Id
    @Column(name="projectIdx",nullable = false,updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int projectIdx;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="userIdx")
    private UserInfo userInfo;

    @Column(name="projectName",nullable = false)
    private String projectName;

    @Column(name="projectMaker",nullable = false)
    private String projectMaker;

    @Column(name="projectStartDate",nullable = false)
    private String projectStartDate;

    @Column(name="projectEndDate",nullable = false)
    private String projectEndDate;

    @Column(name="projectDescription",nullable = false)
    private String projectDescription;

    @Column(name="projectFileURL")
    private String projectFileURL;

    @Column(name="projectBudget",nullable = false)
    private String projectBudget;

    @Column(name="projectSatus")
    private int projectStatus=1;

    @Column(name="status")
    private String status="ACTIVE";

    public Project(UserInfo userInfo, String projectName,
                   String projectMaker, String projectStartDate,
                   String projectEndDate, String projectFileURL,
                   String projectBudget, String projectDescription) {
        this.userInfo = userInfo;
        this.projectName = projectName;
        this.projectBudget = projectBudget;
        this.projectMaker = projectMaker;
        this.projectEndDate = projectEndDate;
        this.projectStartDate = projectStartDate;
        this.projectFileURL = projectFileURL;
        this.projectDescription = projectDescription;
    }


//    public Project(UserInfo userInfo, String projectName, String projectMaker, String projectStartDate, String projectEndDate, String projectFileURL, int projectBudget, String projectDescription) {
//
//
//    }
}
