package com.clnine.kimpd.src.Web.alarm.models;

import com.clnine.kimpd.config.BaseEntity;
import com.clnine.kimpd.src.Web.user.models.UserInfo;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
@Entity
@Data
@Table(name="Alarm")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Alarm extends BaseEntity {
    @Id
    @Column(name="alarmIdx",nullable = false,updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int alarmIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="userIdx")
    private UserInfo userInfo;

    @Column(name="alarmMessage")
    private String alarmMessage;

    @Column(name="readStatus")
    private int readStatus=0;

    @Column(name="status")
    private String status = "ACTIVE";

    public Alarm(UserInfo userInfo, String alarmMessage) {
        this.userInfo = userInfo;
        this.alarmMessage =  alarmMessage;
    }
}
