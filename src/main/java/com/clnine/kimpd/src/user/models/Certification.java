package com.clnine.kimpd.src.user.models;

import com.clnine.kimpd.config.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.intellij.lang.annotations.JdkConstants;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@Table(name="Certification")
public class Certification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="certifiactionIdx",nullable = false,updatable = false)
    private int certificationIdx;

    @Column(name="managerPhoneNum")
    String managerPhoneNum;

    @Column(name="userPhoneNum")
    String userPhoneNum;

    @Column(name="secureCode")
    String secureCode;
}
