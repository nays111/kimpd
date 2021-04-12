package com.clnine.kimpd.src.Web.inquiry.models;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Entity
@Data
@Table(name="InquiryCategory")
public class InquiryCategory {
    @Id
    @Column(name="inquiryCategoryIdx",nullable = false,updatable = false)
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private int inquiryCategoryIdx;

    @Column(name="inquiryCategoryName")
    private String inquiryCategoryName;
}
