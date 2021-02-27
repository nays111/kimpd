package com.clnine.kimpd.src.WebAdmin.casting.models;

import com.clnine.kimpd.config.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@Table(name="Project")
public class AdminProject extends BaseEntity {
    @Id @Column(name="projectIdx",nullable = false,updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int projectIdx;

    @Column(name="projectName")
    private String projectName;

    @Column(name="projectMaker")
    private String projectMaker;

    @Column(name="projectManager")
    private String projectManager;

    @Column(name="projectDescription", nullable = true)
    private String projectDescription;

    @Column(name="projectFileURL",nullable = true)
    private String projectFileURL;

    @Column(name="projectStatus")
    private int projectStatus=1;

    @Column(name="status")
    private String status="ACTIVE";

    public AdminProject(String projectName, String projectMaker, String projectManager,
                        String projectDescription, String projectFileURL, int projectStatus, String status) {
        this.projectName = projectName;
        this.projectMaker = projectMaker;
        this.projectManager = projectManager;
        this.projectDescription = projectDescription;
        this.projectFileURL = projectFileURL;
        this.projectStatus = projectStatus;
        this.status = status;
    }
}
