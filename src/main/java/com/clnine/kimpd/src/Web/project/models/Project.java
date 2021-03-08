package com.clnine.kimpd.src.Web.project.models;

import com.clnine.kimpd.config.BaseEntity;
import com.clnine.kimpd.src.Web.user.models.UserInfo;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

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

    @Column(name="projectMaker")
    private String projectMaker;

    @Column(name="projectStartDate")
    private String projectStartDate;

    @Column(name="projectEndDate")
    private String projectEndDate;

    @Column(name="projectManager")
    private String projectManager;

    @Column(name="projectDescription",nullable = false)
    private String projectDescription;

    @Column(name="projectFileURL")
    private String projectFileURL;

    @Column(name="projectBudget")
    private String projectBudget;

    @Column(name="projectStatus")
    private int projectStatus=1; //프로젝트 완료 여부

    @Column(name="status")
    private String status="ACTIVE"; //프로젝트 삭제 여부

    public Project(UserInfo userInfo, String projectName,
                   String projectMaker, String projectStartDate,
                   String projectEndDate, String projectManager,
                   String projectFileURL, String projectBudget,
                   String projectDescription) {
        this.userInfo = userInfo;
        this.projectName = projectName;
        this.projectBudget = projectBudget;
        this.projectMaker = projectMaker;
        this.projectEndDate = projectEndDate;
        this.projectStartDate = projectStartDate;
        this.projectManager = projectManager;
        this.projectFileURL = projectFileURL;
        this.projectDescription = projectDescription;
    }
    public Project(UserInfo userInfo, String projectName,
                   String projectMaker, String projectStartDate,
                   String projectEndDate, String projectFileURL,
                   String projectDescription) {
        this.userInfo = userInfo;
        this.projectName = projectName;
        this.projectMaker = projectMaker;
        this.projectStartDate = projectStartDate;
        this.projectEndDate = projectEndDate;
        this.projectFileURL = projectFileURL;
        this.projectDescription = projectDescription;
    }
    public Project(UserInfo userInfo,String projectName,String projectDescription){
        this.userInfo = userInfo;
        this.projectName = projectName;
        this.projectDescription = projectDescription;
    }

}
