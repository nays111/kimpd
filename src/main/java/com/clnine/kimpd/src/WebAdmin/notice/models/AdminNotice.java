package com.clnine.kimpd.src.WebAdmin.notice.models;

import com.clnine.kimpd.config.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@Table(name="Notice")
public class AdminNotice extends BaseEntity {
    @Id @Column(name="noticeIdx",nullable = false,updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int noticeIdx;

    @Column(name="noticeTitle")
    private String noticeTitle;

    @Column(name="noticeDescription")
    private String noticeDescription;

    private String status="ACTIVE";

    public AdminNotice(String noticeTitle, String noticeDescription) {
        this.noticeTitle = noticeTitle;
        this.noticeDescription = noticeDescription;
    }
}
