package com.clnine.kimpd.src.Web.user.models;

import com.clnine.kimpd.config.BaseEntity;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static com.clnine.kimpd.config.secret.Secret.aligoSender;

@Entity
@Getter
@NoArgsConstructor
@Table(name="Certification")
@Data
public class Certification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="certifiactionIdx",nullable = false,updatable = false)
    private int certificationIdx;

    @Column(name="managerPhoneNum")
    String managerPhoneNum=aligoSender;

    @Column(name="userPhoneNum")
    String userPhoneNum;

    @Column(name="secureCode")
    int secureCode;

    public Certification(String phoneNum, int rand) {
        this.userPhoneNum=phoneNum;
        this.secureCode = rand;
    }
}
