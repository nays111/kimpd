package com.clnine.kimpd.src.user.models;

import com.clnine.kimpd.config.BaseEntity;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PUBLIC) // Unit Test 를 위해 PUBLIC
@EqualsAndHashCode(callSuper = false)
@Data // from lombok
@Entity // 필수, Class 를 Database Table화 해주는 것이다
@Table(name = "UserInfo") // Table 이름을 명시해주지 않으면 class 이름을 Table 이름으로 대체한다.
public class UserInfo extends BaseEntity {
    /**
     * 유저 인덱스
     */
    @Id // PK를 의미하는 어노테이션
    @Column(name = "userIdx", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userIdx;

    @Column(name="userType",nullable = false)
    private int userType;

    @Column(name="id",nullable = false)
    private String id;

    @Column(name="password",nullable = false)
    private String password;

    @Column(name="email",nullable = false)
    private String email;

    @Column(name="phoneNum",nullable = false)
    private String phoneNum;

    @Column(name="address")
    private String address;

    @Column(name="agreeAdvertisement")
    private int agreeAdvertisement;

    /**
     * 개인사업자
     */
   private String businessNumber;


    private String businessImageURL;


    private String corporationBusinessName;


    private String corporationBusinessNumber;


    private String nickname;


    private String profileImageURL;


    private String status="ACTIVE";

    public UserInfo(int userType, String id, String password, String email, String phoneNumber) {
        this.userType = userType;
        this.id = id;
        this.password = password;
        this.email = email;
        this.phoneNum=phoneNumber;
    }
}
