package com.clnine.kimpd.src.WebAdmin.faq.models;

import com.clnine.kimpd.config.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@Table(name="Notice")
public class AdminFaq extends BaseEntity {
    @Id @Column(name="noticeIdx",nullable = false,updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int noticeIdx;

    @Column(name="noticeTitle")
    private String noticeTitle;

    @Column(name="noticeDescription")
    private String noticeDescription;

    private String status="ACTIVE";

    public AdminFaq(String noticeTitle, String noticeDescription) {
        this.noticeTitle = noticeTitle;
        this.noticeDescription = noticeDescription;
    }
}
