package com.clnine.kimpd.src.Web.inquiry.models;

import com.clnine.kimpd.config.BaseEntity;
import com.clnine.kimpd.src.Web.user.models.UserInfo;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Table(name="Inquiry")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Inquiry extends BaseEntity {
    @Id
    @Column(name="inquiryIdx",nullable = false,updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int inquiryIdx;

    @Column(name="inquiryTitle")
    private String inquiryTitle;

    //todo 질문 유형 테이블 컬럼 FK추가
    @Column(name="inquiryDescription")
    private String inquiryDescription;

    @Column(name="inquiryAnswer")
    private String inquiryAnswer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="userIdx")
    private UserInfo userInfo;

    @Column(name="status")
    private String status = "ACTIVE";
}
