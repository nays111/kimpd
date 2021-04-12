package com.clnine.kimpd.src.Web.message.models;

import com.clnine.kimpd.config.BaseEntity;
import com.clnine.kimpd.src.Web.user.models.UserInfo;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Table(name="Message")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Message extends BaseEntity {

    @Id
    @Column(name="messageIdx",nullable = false,updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int messageIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="senderIdx",referencedColumnName = "userIdx")
    private UserInfo sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="receiverIdx",referencedColumnName = "userIdx")
    private UserInfo receiver;

    @Column(name="description")
    private String description;

    @Column(name="readStatus")
    private int readStatus=0;

    @Column(name="status")
    private String status="ACTIVE";

    public Message(UserInfo senderInfo, UserInfo receiverInfo, String description) {
        this.sender = senderInfo;
        this.receiver = receiverInfo;
        this.description = description;
    }
}
