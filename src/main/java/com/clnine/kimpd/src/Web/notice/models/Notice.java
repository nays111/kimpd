package com.clnine.kimpd.src.Web.notice.models;

import com.clnine.kimpd.config.BaseEntity;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name="Notice")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Notice extends BaseEntity {
    @Id
    @Column(name="noticeIdx",nullable = false,updatable = false)
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private int noticeIdx;

    @Column(name="noticeTitle")
    private String noticeTitle;

    @Column(name="noticeDescription")
    private String noticeDescription;

    @Column(name="status")
    private String status="ACTIVE";
}
